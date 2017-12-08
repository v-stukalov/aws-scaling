package aws.scaling.thumbnail;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;

@Service
@Profile("processor")
public class ProcessingService {

    private final Logger log = Logger.getLogger(ProcessingService.class);

    @Autowired
    private JmsTemplate defaultJmsTemplate;

    @Autowired
    private ThumbnailCreatorComponent thumbnailCreator;

    @Autowired
    private NotificationComponent notification;

    @JmsListener(destination = "${thumbnail.requests.queue}")
    public void createThumbnail(String requestJSON) throws JMSException {
        log.info("Request received.");
        try {
            ThumbnailRequest request = ThumbnailRequest.fromJSON(requestJSON);
            String thumbnailUrl =
                    thumbnailCreator.createThumbnail(request.getImageUrl());
            notification.processingComplete(new ThumbnailResult(request, thumbnailUrl));
        } catch (IOException ex) {
            log.error("Encountered error while parsing message.", ex);
            throw new JMSException("Encountered error while parsing message.");
        }
    }
}
