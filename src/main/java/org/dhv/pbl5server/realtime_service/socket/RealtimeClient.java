package org.dhv.pbl5server.realtime_service.socket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
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

    private String clientId;
    private final Socket socket;
    private OnClientConnectCallback onConnect;
    private ValueChange<String> onDisconnect;

    @Setter(AccessLevel.NONE)
    private DataOutputStream out;
    @Setter(AccessLevel.NONE)
    private DataInputStream in;

    @lombok.Builder
    public RealtimeClient(Socket socket, OnClientConnectCallback onConnect, ValueChange<String> onDisconnect) {
        this.socket = socket;
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
        this.setName("Anonymous client socket %s".formatted(CommonUtils.getCurrentTimestamp()));
    }

    @Override
    public synchronized void run() {
        // Check socket
        if (socket == null) {
            log("Socket client must not be null!", true);
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
            // Check account id
            this.clientId = generateClientId(accountId);
            if (clientId == null) {
                log("Invalid account id", true);
                disconnect();
                return;
            }
            this.setName(clientId);
            log("Connected", false);
            onConnect.call(clientId, this);
        } catch (InterruptedException | IOException e) {
            log(e instanceof EOFException ? "Required account id" : e.getMessage(), true);
            disconnect();
        }
    }

    @Override
    public void send(String type, String message) {
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
        if (socket == null || !CommonUtils.isValidUuid(accountId)) return null;
        return "%s:%s".formatted(accountId, socket.getInetAddress().getHostAddress()).replaceAll(" ", "-");
    }

    private void handleException(VoidCallback callBack, VoidCallback onError) {
        try {
            callBack.call();
        } catch (Exception e) {
            log(e.getMessage(), true);
        }
    }

    private void log(String msg, boolean isError) {
        String prefix = "Client socket: %s ===>".formatted(this.clientId);
        if (isError) {
            log.error("{} {}", prefix, msg);
            return;
        }
        log.info("{} {}", prefix, msg);
    }
}
