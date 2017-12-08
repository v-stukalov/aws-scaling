package aws.scaling.thumbnail;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.UUID;

@Service
public class ThumbnailService {

    private final Logger log = Logger.getLogger(ThumbnailService.class);

    @Autowired
    private JmsTemplate defaultJmsTemplate;

    @Value("${thumbnail.requests.queue}")
    private String THUMBNAIL_REQUESTS_QUEUE;

    public void requestThumbnail(String fileName) {
        ThumbnailRequest request = new ThumbnailRequest(UUID.randomUUID().toString(), fileName);
        defaultJmsTemplate.convertAndSend(THUMBNAIL_REQUESTS_QUEUE,
                request.toJSON());
        System.out.println("Request sent.");
    }

    @JmsListener(destination = "${thumbnail.results.queue}")
    public void createThumbnail(String resultJSON) throws JMSException {
        log.info("Notification received.");
        try {
            ThumbnailResult request = ThumbnailResult.fromJSON(resultJSON);
            System.out.println("Result: " + request.getThumbnailUrl());
        } catch (IOException ex) {
            log.error("Encountered error while parsing message.", ex);
            throw new JMSException("Encountered error while parsing message.");
        }
    }
}
