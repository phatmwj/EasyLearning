package tpp.easy.learning.data.socket;


import tpp.easy.learning.data.socket.dto.Message;

public interface KittyRealtimeEvent {
    void onMessageReceived(Message message);
    void onConnectionClosed();
    void onConnectionClosing();
    void onConnectionFailed();
    void onConnectionOpened();
}
