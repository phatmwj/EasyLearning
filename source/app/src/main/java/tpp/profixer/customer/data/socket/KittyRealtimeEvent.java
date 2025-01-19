package tpp.profixer.customer.data.socket;


import tpp.profixer.customer.data.socket.dto.Message;

public interface KittyRealtimeEvent {
    void onMessageReceived(Message message);
    void onConnectionClosed();
    void onConnectionClosing();
    void onConnectionFailed();
    void onConnectionOpened();
}
