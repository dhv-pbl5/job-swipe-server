package org.dhv.pbl5server.realtime_service.call_back;

@FunctionalInterface
public interface ValueChange<T> {
    void call(T value);
}
