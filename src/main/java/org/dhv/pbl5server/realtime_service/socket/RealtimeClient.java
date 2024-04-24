package org.dhv.pbl5server.realtime_service.socket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.UnauthorizedException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.realtime_service.call_back.OnClientConnectCallback;
import org.dhv.pbl5server.realtime_service.call_back.ValueChange;
import org.dhv.pbl5server.realtime_service.call_back.VoidCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;


@Getter
@Setter
@Slf4j
public class RealtimeClient extends Thread implements AbstractClient {

    private final int delayBeforeConnectToClient = 500; // In milliseconds
    private String clientId;
    private final Socket socket;
    private final JwtService jwtService;
    private OnClientConnectCallback onConnect;
    private ValueChange<String> onDisconnect;

    @Setter(AccessLevel.NONE)
    private DataOutputStream out;
    @Setter(AccessLevel.NONE)
    private DataInputStream in;

    @lombok.Builder
    public RealtimeClient(
        Socket socket,
        JwtService jwtService,
        OnClientConnectCallback onConnect, ValueChange<String> onDisconnect) {
        this.socket = socket;
        this.jwtService = jwtService;
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
        // Set default name for thread
        this.setName("Anonymous client socket %s".formatted(CommonUtils.getCurrentTimestamp()));
    }

    @Override
    public synchronized void run() {
        // Check socket is null
        if (socket == null) {
            LogUtils.error(getDebugPrefix(), "Socket client is null!");
            return;
        }
        // Required account id from client in order to use realtime service
        // Delay ... milliseconds before receiving value from client
        try {
            // Initialize streams
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            out.writeUTF("Please send your access_token...! (Your connection time out is %d in milliseconds)".formatted(delayBeforeConnectToClient));
            Thread.sleep(delayBeforeConnectToClient);
            // Get account id
            if (in.available() == 0) throw new EOFException("Required account id");
            String token = in.readUTF();
            // Generate client id
            this.clientId = generateClientId(token);
            if (CommonUtils.isNotEmptyOrNullString(clientId)) {
                this.setName(clientId);
                LogUtils.info(getDebugPrefix(), "Connected");
                // Call onConnect callback
                onConnect.call(clientId, this);
            }
        } catch (InterruptedException | IOException e) {
            if (e instanceof EOFException) LogUtils.error(getDebugPrefix(), "Required account id");
            else LogUtils.error(getDebugPrefix(), e);
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
            socket.close();
            out.close();
            in.close();
        }, null);
        // Call onDisconnect callback
        if (CommonUtils.isNotEmptyOrNullString(clientId))
            onDisconnect.call(clientId);
        LogUtils.info(getDebugPrefix(), "Disconnected");
        this.interrupt();
    }

    /**
     * @return client id with format: accountId:ipAddress
     * eg: 123e4567-e89b-12d3-a456-426614174000:127.0.0.0
     */
    private String generateClientId(String token) {
        String logMessage = "";
        String socketErrorMessage = "";
        if (socket == null) // Check socket is null
            logMessage = "Socket client is null!";
        else if (jwtService == null) // Check account repository is null
            logMessage = "Jwt service is null!";
        // Get account from token
        try {
            assert jwtService != null;
            assert socket != null;
            Account account = (Account) jwtService.getAccountFromToken(token);
            // Generate client id
            return "%s:%s".formatted(
                account.getAccountId().toString(),
                socket.getInetAddress().getHostAddress()
            ).replaceAll(" ", "-");
        } catch (UnauthorizedException | ForbiddenException ex) {
            var error = ErrorUtils.getExceptionError(ex.getMessage());
            socketErrorMessage = CommonUtils.convertToJson(error);
            logMessage = error.getMessage();
        }
        // Error message
        LogUtils.error(getDebugPrefix(), logMessage);
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
                    LogUtils.error(getDebugPrefix(), ex);
                }
            }
            LogUtils.error(getDebugPrefix(), e);
        }
    }

    private String getDebugPrefix() {
        return "Client socket: %s".formatted(this.clientId == null ? this.getName() : this.clientId);
    }
}
