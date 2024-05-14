package org.dhv.pbl5server.realtime_service.call_back;

// git commit -m "PBL-593 config realtime"

@FunctionalInterface
public interface VoidCallback {
    void call() throws Exception;
}
