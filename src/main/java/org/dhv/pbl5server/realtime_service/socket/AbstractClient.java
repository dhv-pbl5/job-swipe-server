package org.dhv.pbl5server.realtime_service.socket;

import java.io.DataInputStream;

// git commit -m "PBL-593 config realtime"

public interface AbstractClient {
    void send(Object message);

    void send(Object type, Object message);

    String handleReceivedData(DataInputStream in);

    void disconnect();
}
