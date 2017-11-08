package aws.scaling.thumbnail;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;

@Service
public class ThumbnailService {

    private final Logger log = Logger.getLogger(ThumbnailService.class);

    @Autowired
    private ThumbnailCreatorComponent thumbnailCreator;

    @Autowired
    private NotificationComponent notification;

    @JmsListener(destination = "thumbnail_requests")
    public void createThumbnail(String requestJSON) throws JMSException {
        log.info("Received ");
        try {
            ThumbnailRequest request = ThumbnailRequest.fromJSON(requestJSON);
            String thumbnailUrl =
                    thumbnailCreator.createThumbnail(request.getImageUrl());
            notification.thumbnailComplete(new ThumbnailResult(request, thumbnailUrl));
        } catch (IOException ex) {
            log.error("Encountered error while parsing message.", ex);
            throw new JMSException("Encountered error while parsing message.");
        }
    }

}
