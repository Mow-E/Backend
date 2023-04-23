package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import se.mow_e.models.Coordinate;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.websocket.messages.CoordinateMessage;

import java.util.Optional;

@Controller
public class MowerController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CoordinateRepo coordinateRepo;

    @MessageMapping("/coordinate")
    public void getCoordinate(CoordinateMessage message){
        try {
//            System.out.println(message);
            Coordinate entityCoordinate = new Coordinate(  // TODO mappings
                    0L,
                    message.getMowerId(),
                    message.getX(),
                    message.getY(),
                    message.getTime(),
                    message.getStateId(),
                    null,
                    message.getExtra()  //  Temporary id to bind the image to right coordinate
            );
            if(message.getState() == CoordinateMessage.State.START){
                Optional<Long> lastSessionId = coordinateRepo.findLastSessionId(message.getMowerId());
                if( lastSessionId.isPresent()){
                    entityCoordinate.setSessionId(lastSessionId.get() + 1);
                } else {
                    entityCoordinate.setSessionId(1L);
                }
            } else {
                coordinateRepo.findLastSessionId(message.getMowerId())
                        .ifPresent(entityCoordinate::setSessionId); // Java reference voodoo magic - no comments, just google
            }

            coordinateRepo.save(entityCoordinate);
            messagingTemplate.convertAndSendToUser(message.getMowerId(),"/queue/coordinate", message);   //  TODO    CoordinateNotification
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
