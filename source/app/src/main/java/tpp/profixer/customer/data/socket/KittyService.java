package tpp.profixer.customer.data.socket;

import com.tinder.scarlet.WebSocket;
import com.tinder.scarlet.ws.Receive;
import com.tinder.scarlet.ws.Send;

import io.reactivex.Flowable;
import tpp.profixer.customer.data.socket.dto.Message;

public interface KittyService {
    @Receive
    Flowable<WebSocket.Event> observeWebSocketEvent();
    @Send
    void request(Message data);
    @Receive
    Flowable<Message> message();
}
