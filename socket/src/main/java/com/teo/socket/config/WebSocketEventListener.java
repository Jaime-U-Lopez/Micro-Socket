package com.teo.socket.config;

import com.teo.socket.chat.ChatMessage;
import com.teo.socket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    // cuando queremos escuchar un evento distinto, y tambien para controlar cuando un usuario se salga del chat
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){


        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){

            log.info("User disconnected: {}", username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVER)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public", chatMessage);

        }
        //Todo implementar cuando un usuario salda del chat
    }
}
