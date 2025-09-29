package com.example.awslabs.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqsLambdaHandler implements RequestHandler<SQSEvent, String> {

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        log.info("Lambda triggered with {} messages", event.getRecords().size());
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            log.info("Message ID: {}", msg.getMessageId());
            log.info("Message Body: {}", msg.getBody());
        }
        return "Processed " + event.getRecords().size() + " messages.";
    }

}
