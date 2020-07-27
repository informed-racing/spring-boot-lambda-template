package informed.racing;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application implements RequestHandler<SQSEvent, Void> {

    private static final Logger LOG = LogManager.getLogger(Application.class);
    private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Void handleRequest(SQSEvent sqs, Context context) {
//        sqs.getRecords().forEach(record -> {
//            LOG.info("Handling request: {}", record.getMessageId());
//            try {
//                OperationRequest request = mapper.readValue(record.getBody(), OperationRequest.class);
//                handleRequest(record.getMessageId(), request);
//                LOG.info("Request submitted: {}", record.getMessageId());
//            } catch (JsonProcessingException ignored) {
//                LOG.error("Request failed to parse: {} {}", record.getMessageId(), record.getBody());
//            }
//        });
        return null;
    }

}
