package informed.racing.functions;


import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.TraceHeader;
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
            xrayContextRecovery(record.getAttributes().get("AWSTraceHeader"));
        });
        return "";
    }


    private void xrayContextRecovery(String awsTraceHeader) {
        if (awsTraceHeader != null) {
            TraceHeader traceHeader = TraceHeader.fromString(awsTraceHeader);
            Segment segment = AWSXRay.getCurrentSegment();
            segment.setTraceId(traceHeader.getRootTraceId());
            segment.setParentId(traceHeader.getParentId());
            segment.setSampled(traceHeader.getSampled().equals(TraceHeader.SampleDecision.SAMPLED));
        }
    }
}