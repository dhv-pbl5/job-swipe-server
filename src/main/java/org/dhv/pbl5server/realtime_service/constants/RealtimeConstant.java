package org.dhv.pbl5server.realtime_service.constants;

// git commit -m "PBL-593 config realtime"
// git commit -m "PBL-594 realtime matching for company"

public class RealtimeConstant {

    /*
     * SERVER SOCKET
     */
    public static final String SERVER_DEBUG_PREFIX = "SERVER SOCKET";
    public static final String SERVER_RUNNING = "Running on port";
    public static final String ACCEPTING_CLIENT_ERROR = "Error while accepting client socket";

    /*
     * CLIENT SOCKET
     */
    public static final String CLIENT_DEBUG_PREFIX = "CLIENT SOCKET";
    public static final String ANONYMOUS_CLIENT_NAME = "Anonymous client socket %s";
    public static final String CLIENT_SOCKET_NULL = "Socket client is null!";
    public static final String REQUIRED_TOKEN = "Token is required!";
    public static final String INTRODUCTION_MESSAGE = "Please send your access_token...! (Your connection time out is %d in milliseconds)";
    public static final String CLIENT_CONNECTED = "Connected";
    public static final String CLIENT_DISCONNECTED = "Disconnected";
    public static final String JWT_SERVICE_NULL = "Jwt service is null!";
    public static final String PONG = "PONG";
    public static final String PING = "PING";

    private RealtimeConstant() {
    }
}
