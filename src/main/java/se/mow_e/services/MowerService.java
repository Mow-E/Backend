package se.mow_e.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.mow_e.models.Coordinate;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.websocket.messages.CoordinateMessage;

import java.util.Optional;

@Service
public class MowerService {

    @Autowired
    private CoordinateRepo coordinateRepo;


    public void saveCoordinate(CoordinateMessage message) {

//            System.out.println(message);
            Coordinate entityCoordinate = new Coordinate(  // TODO mappings
                    0L,
                    message.getMowerId(),
                    message.getX(),
                    message.getY(),
                    message.getZ(),
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
    }

}
