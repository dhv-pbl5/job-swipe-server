package org.dhv.pbl5server.realtime_service.socket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.dhv.pbl5server.realtime_service.call_back.OnClientConnectCallback;
import org.dhv.pbl5server.realtime_service.call_back.ValueChange;
import org.dhv.pbl5server.realtime_service.call_back.VoidCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;


@Getter
@Setter
@Slf4j
public class RealtimeClient extends Thread implements AbstractClient {

    private String clientId;
    private final Socket socket;
    private final AccountRepository accountRepository;
    private OnClientConnectCallback onConnect;
    private ValueChange<String> onDisconnect;

    @Setter(AccessLevel.NONE)
    private DataOutputStream out;
    @Setter(AccessLevel.NONE)
    private DataInputStream in;

    @lombok.Builder
    public RealtimeClient(Socket socket, AccountRepository accountRepository, OnClientConnectCallback onConnect, ValueChange<String> onDisconnect) {
        this.socket = socket;
        this.accountRepository = accountRepository;
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
        // Set default name for thread
        this.setName("Anonymous client socket %s".formatted(CommonUtils.getCurrentTimestamp()));
    }

    @Override
    public synchronized void run() {
        // Check socket is null
        if (socket == null) {
            log("Socket client is null!", true);
            return;
        }
        // Required account id from client in order to use realtime service
        // Delay 500 milliseconds before receiving value from client
        try {
            Thread.sleep(500);
            // Initialize streams
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            // Get account id
            String accountId = in.readUTF();
            // Generate client id
            this.clientId = generateClientId(accountId);
            if (CommonUtils.isNotEmptyOrNullString(clientId)) {
                this.setName(clientId);
                log("Connected", false);
                // Call onConnect callback
                onConnect.call(clientId, this);
            }
        } catch (InterruptedException | IOException e) {
            log(e instanceof EOFException ? "Required account id" : e.getMessage(), true);
            disconnect();
        }
    }

    @Override
    public void send(Object type, Object message) {
        handleException(
            () -> out.writeUTF(RealtimeModel.toJson(type, message)),
            this::disconnect);
    }

    @Override
    public void disconnect() {
        handleException(() -> {
            out.close();
            in.close();
            socket.close();
        }, null);
        // Call onDisconnect callback
        if (CommonUtils.isNotEmptyOrNullString(clientId))
            onDisconnect.call(clientId);
        log("Disconnected", false);
        this.interrupt();
    }

    /**
     * @return client id with format: accountId:ipAddress
     * eg: 123e4567-e89b-12d3-a456-426614174000:127.0.0.0
     */
    private String generateClientId(String accountId) {
        String logMessage = "";
        String socketErrorMessage = "";
        if (socket == null) // Check socket is null
            logMessage = "Socket client is null!";
        else if (accountRepository == null) // Check account repository is null
            logMessage = "Account repository is null!";
        else if (!CommonUtils.isValidUuid(accountId)) { // Check account id is invalid
            logMessage = "Account's id is invalid!";
            socketErrorMessage = ErrorUtils.getExceptionError(ErrorMessageConstant.ACCOUNT_ID_IS_REQUIRED).getMessage();
        } else if (!accountRepository.existsById(UUID.fromString(accountId))) { // Check account id is not existed in database
            logMessage = "Account is not found!";
            socketErrorMessage = ErrorUtils.getExceptionError(ErrorMessageConstant.ACCOUNT_NOT_FOUND).getMessage();
        }
        if (CommonUtils.isEmptyOrNullString(logMessage) && CommonUtils.isEmptyOrNullString(socketErrorMessage)) {
            assert socket != null;
            return "%s:%s".formatted(accountId, socket.getInetAddress().getHostAddress()).replaceAll(" ", "-");
        }
        // Error message
        log(logMessage, true);
        if (CommonUtils.isNotEmptyOrNullString(socketErrorMessage) && out != null) {
            String tmp = socketErrorMessage;
            handleException(() -> out.writeUTF(tmp), null);
        }
        disconnect();
        return null;
    }

    private void handleException(VoidCallback callBack, VoidCallback onError) {
        try {
            if (callBack != null) callBack.call();
        } catch (Exception e) {
            if (onError != null) {
                try {
                    onError.call();
                } catch (Exception ex) {
                    log(ex.getMessage(), true);
                }
            }
            log(e.getMessage(), true);
        }
    }

    private void log(String msg, boolean isError) {
        if (CommonUtils.isEmptyOrNullString(msg)) return;
        String prefix = "Client socket: %s ===>".formatted(this.clientId == null ? this.getName() : this.clientId);
        if (isError) {
            log.error("{} {}", prefix, msg);
        } else {
            log.info("{} {}", prefix, msg);
        }
    }
}
