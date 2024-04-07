package org.dhv.pbl5server.realtime_service.call_back;

@FunctionalInterface
public interface VoidCallback {
    void call() throws Exception;
}
