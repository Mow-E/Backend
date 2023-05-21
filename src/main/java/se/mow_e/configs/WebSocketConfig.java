package se.mow_e.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import se.mow_e.services.JwtService;

import java.security.Principal;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {  // TODO Fix Security, jwt

    private final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private JwtService jwtService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*");
        registry.addEndpoint("/websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/mower");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/mower");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor == null) return null;
//                log.info("in override " + accessor.getCommand());

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authToken = accessor.getFirstNativeHeader("x-auth-token");
//                    log.info("Header auth token: " + authToken);

                    if (!jwtService.validateToken(authToken)) return null;
                    Principal principal = jwtService.getAuthentication(authToken);


                    accessor.setUser(principal);
                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//                    if (Objects.nonNull(authentication)) {
//                        log.info("Disconnected Auth : " + authentication.getName());
//                    } else {
//                        log.info("Disconnected Sess : " + accessor.getSessionId());
//                    }

                }
                return message;
            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

                // ignore non-STOMP messages like heartbeat messages
                if (sha.getCommand() == null) {
                    log.warn("postSend null command");
                    return;
                }

                String sessionId = sha.getSessionId();

                switch (sha.getCommand()) {
                    case CONNECT:
                        log.info("STOMP Connect [sessionId: " + sessionId + "]");
                        break;
                    case CONNECTED:
                        log.info("STOMP Connected [sessionId: " + sessionId + "]");
                        break;
                    case DISCONNECT:
                        log.info("STOMP Disconnect [sessionId: " + sessionId + "]");
                        break;
                    default:
                        break;

                }
            }
        });

    }

}
