package se.mow_e.controllers.ws;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import se.mow_e.services.MowerService;
import se.mow_e.websocket.messages.CoordinateMessage;

@Controller
public class MowerController {

    private final Logger log = LoggerFactory.getLogger(MowerController.class);

    @Autowired
    private MowerService mowerService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/coordinate")
    public void getCoordinate(CoordinateMessage message) {

        messagingTemplate.convertAndSendToUser(message.getMowerId(), "/queue/coordinate", message);   //  TODO    CoordinateNotification

        try {
            mowerService.saveCoordinate(message);
        } catch (Exception e) {
            log.error("Error while saving the coordinate", e);
        }

    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
