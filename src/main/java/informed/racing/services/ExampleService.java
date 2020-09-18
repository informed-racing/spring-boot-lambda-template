package informed.racing.services;


import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExampleService {

    public String handle(SQSEvent sqsEvent) {
        sqsEvent.getRecords().forEach(record -> {
            log.info("message received: {}", record.getMessageId());
        });
        return "";
    }

}