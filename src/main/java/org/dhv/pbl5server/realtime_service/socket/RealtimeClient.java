package org.dhv.pbl5server.realtime_service.socket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.UnauthorizedException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.realtime_service.call_back.OnClientConnectCallback;
import org.dhv.pbl5server.realtime_service.call_back.ValueChange;
import org.dhv.pbl5server.realtime_service.call_back.VoidCallback;
import org.dhv.pbl5server.realtime_service.constants.RealtimeConstant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
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
        this.setName(RealtimeConstant.ANONYMOUS_CLIENT_NAME.formatted(CommonUtils.getCurrentTimestamp()));
    }

    @Override
    public void run() {
        // Check socket is null
        if (socket == null) {
            LogUtils.error(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), RealtimeConstant.CLIENT_SOCKET_NULL);
            return;
        }
        // Required account id from client in order to use realtime service
        // Delay ... milliseconds before receiving value from client
        try {
            // Initialize streams
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            send(RealtimeConstant.INTRODUCTION_MESSAGE.formatted(delayBeforeConnectToClient));
            Thread.sleep(delayBeforeConnectToClient);
            // Get access token
            if (in.available() == 0)
                throw new EOFException(RealtimeConstant.REQUIRED_TOKEN);
            String token = handleReceivedData(in);
            // Generate client id
            this.clientId = generateClientId(token);
            if (CommonUtils.isNotEmptyOrNullString(clientId)) {
                this.setName(clientId);
                LogUtils.info(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), RealtimeConstant.CLIENT_CONNECTED);
                send(RealtimeConstant.CLIENT_CONNECTED);
                // Call onConnect callback
                onConnect.call(clientId, this);
            }
            // Listen to client
            while (!socket.isClosed()) {
                String msg = handleReceivedData(in);
                if (msg.equalsIgnoreCase(RealtimeConstant.PING)) {
                    send(RealtimeConstant.PONG);
                    continue;
                }
                if (CommonUtils.isNotEmptyOrNullString(msg))
                    LogUtils.info(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), msg);
            }
            disconnect();
        } catch (InterruptedException | IOException e) {
            if (e instanceof EOFException)
                LogUtils.error(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), RealtimeConstant.REQUIRED_TOKEN);
            else
                LogUtils.error(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), e);
            disconnect();
        }
    }

    @Override
    public void send(Object message) {
        handleException(
                () -> out.writeBytes("%s%s".formatted(message, CommonConstant.NOTIFICATION_DATA_END_SYMBOL)),
                this::disconnect);
    }

    @Override
    public void send(Object type, Object message) {
        handleException(
                () -> out.writeBytes(RealtimeModel.toJson(type, message)),
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
        LogUtils.info(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), RealtimeConstant.CLIENT_DISCONNECTED);
        this.interrupt();
    }

    @Override
    @SuppressWarnings("unused")
    public String handleReceivedData(DataInputStream in) {
        AtomicReference<String> result = new AtomicReference<>("");
        handleException(() -> {
            byte[] buffer = new byte[in.available()];
            int ignoredResult = in.read(buffer);
            result.set(new String(buffer, StandardCharsets.UTF_8));
        }, null);
        return result.get();
    }

    /**
     * @return client id with format: accountId:ipAddress
     *         eg: 123e4567-e89b-12d3-a456-426614174000:127.0.0.0
     */
    private String generateClientId(String token) {
        String logMessage = "";
        String socketErrorMessage = "";
        if (socket == null) // Check socket is null
            logMessage = RealtimeConstant.CLIENT_SOCKET_NULL;
        else if (jwtService == null) // Check account repository is null
            logMessage = RealtimeConstant.JWT_SERVICE_NULL;
        // Get account from token
        try {
            assert jwtService != null;
            assert socket != null;
            Account account = (Account) jwtService.getAccountFromToken(token);
            // Generate client id
            return "%s:%s".formatted(
                    account.getAccountId().toString(),
                    socket.getInetAddress().getHostAddress()).replaceAll(" ", "-");
        } catch (UnauthorizedException | ForbiddenException ex) {
            var error = ErrorUtils.getExceptionError(ex.getMessage());
            socketErrorMessage = CommonUtils.convertToJson(error);
            logMessage = error.getMessage();
        }
        // Error message
        LogUtils.error(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), logMessage);
        if (CommonUtils.isNotEmptyOrNullString(socketErrorMessage) && out != null) {
            String tmp = socketErrorMessage;
            handleException(() -> send(tmp), null);
        }
        disconnect();
        return null;
    }

    private void handleException(VoidCallback callBack, VoidCallback onError) {
        try {
            if (callBack != null)
                callBack.call();
        } catch (Exception e) {
            if (onError != null) {
                try {
                    onError.call();
                } catch (Exception ex) {
                    LogUtils.error(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), ex);
                }
            }
            LogUtils.error(RealtimeConstant.CLIENT_DEBUG_PREFIX, getDebugMsg(), e);
        }
    }

    private String getDebugMsg() {
        return this.clientId == null ? this.getName() : this.clientId;
    }
}
