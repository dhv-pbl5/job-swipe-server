package org.dhv.pbl5server.realtime_service.call_back;

// git commit -m "PBL-593 config realtime"

@FunctionalInterface
public interface ValueChange<T> {
    void call(T value);
}
