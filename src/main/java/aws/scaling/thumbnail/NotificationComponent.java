package aws.scaling.thumbnail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("processor")
class NotificationComponent {

    @Autowired
    private JmsTemplate defaultJmsTemplate;

    @Value("${thumbnail.results.queue}")
    private String THUMBNAIL_RESULTS_QUEUE;

    public void processingComplete(ThumbnailResult result) throws IOException {
        defaultJmsTemplate.convertAndSend(THUMBNAIL_RESULTS_QUEUE,
                result.toJSON());
        System.out.println("Notification sent.");
    }

}
