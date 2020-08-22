package informed.racing.functions;


import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Log4j2
@Component
public class TemplateFunction implements Function<SQSEvent, String> {

    private final ObjectMapper objectMapper;

    public TemplateFunction(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String apply(SQSEvent sqs) {
        log.info("scraperJobQueue: {}", sqs);
        sqs.getRecords().forEach(record -> {
            log.info("message received: {}", record.getMessageId());
        });
        return "";
    }

}