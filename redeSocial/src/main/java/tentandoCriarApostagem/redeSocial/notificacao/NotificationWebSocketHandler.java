package tentandoCriarApostagem.redeSocial.notificacao;


import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    // Mapeia o ID do usuário para a sessão WebSocket
    private Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Quando o cliente se conecta, a sessão WebSocket é associada ao usuário
        String username = getUsernameFromSession(session);  // Exemplo: pegar o nome de usuário da sessão
        userSessions.put(username, session);
        System.out.println("Conexão estabelecida para o usuário: " + username);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Aqui você pode adicionar lógica para tratar mensagens recebidas se necessário
        System.out.println("Mensagem recebida de " + session.getId() + ": " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Quando a conexão for fechada, remova a sessão
        String username = getUsernameFromSession(session);  // Exemplo: pegar o nome de usuário da sessão
        userSessions.remove(username);
        System.out.println("Conexão fechada para o usuário: " + username);
    }

    public void sendNotificationToUser(String username, String message) {
        WebSocketSession session = userSessions.get(username);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Função fictícia para extrair o nome de usuário da sessão, pode ser implementada de acordo com a autenticação do sistema
    private String getUsernameFromSession(WebSocketSession session) {
        // Exemplo: extrair o nome do usuário a partir de um atributo na sessão, como um token de autenticação
        return (String) session.getAttributes().get("username");
    }
}
