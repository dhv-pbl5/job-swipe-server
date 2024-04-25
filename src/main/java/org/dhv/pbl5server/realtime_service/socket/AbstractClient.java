package org.dhv.pbl5server.realtime_service.socket;

import java.io.DataInputStream;

public interface AbstractClient {
    void send(Object message);

    void send(Object type, Object message);

    String handleReceivedData(DataInputStream in);

    void disconnect();
}
