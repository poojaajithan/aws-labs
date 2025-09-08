package com.example.awslabs.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

@Slf4j
@Service
public class SQSService {

	private final SqsClient sqsClient;
	
	public SQSService(SqsClient sqsClient) {
		this.sqsClient = sqsClient;
	}

	public void sendMessage(String queueUrl, String messageBody) {
		SendMessageRequest request = SendMessageRequest.builder()
														.queueUrl(queueUrl)
														.messageBody(messageBody)
														.build();
		sqsClient.sendMessage(request);
		log.info("Message sent to queue {}: {}", queueUrl, messageBody);
	}
	
	public List<Message> receiveMessages(String queueUrl) {
		ReceiveMessageRequest request = ReceiveMessageRequest.builder()
																.queueUrl(queueUrl)
																.maxNumberOfMessages(5)
																.waitTimeSeconds(10) //long polling
																.build();
		List<Message> messages = sqsClient.receiveMessage(request).messages();
		log.info("Received {} messages from queue {}", messages.size(), queueUrl);
        return messages;
	}
	
	public void deleteMessage(String queueUrl, String receiptHandle)
	{
		DeleteMessageRequest request = DeleteMessageRequest.builder()
															.queueUrl(queueUrl)
															.receiptHandle(receiptHandle)
															.build();
		sqsClient.deleteMessage(request);
		log.info("Deleted message from queue {}", queueUrl);
	}

    public List<String> listQueues() {
        List<String> queueUrls = sqsClient.listQueues().queueUrls();
		log.info("Retrieved {} queues", queueUrls.size());
		return queueUrls;
    }

    public String createQueue(String queueName) {
        CreateQueueRequest request = CreateQueueRequest.builder()
                .queueName(queueName)
                .build();
        CreateQueueResponse response = sqsClient.createQueue(request);
        log.info("Created queue {}: {}", queueName, response.queueUrl());
        return response.queueUrl();
    }

    public void deleteQueue(String queueUrl) {
        DeleteQueueRequest request = DeleteQueueRequest.builder()
                .queueUrl(queueUrl)
                .build();
        sqsClient.deleteQueue(request);
        log.info("Deleted queue: {}", queueUrl);
    }

    public void sendMessageWithAttributes(String queueUrl, String messageBody, Map<String, String> attributes) {
        SendMessageRequest.Builder builder = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody);

        if (attributes != null && !attributes.isEmpty()) {
            attributes.forEach((key, value) -> builder.messageAttributesEntry(
                    key, MessageAttributeValue.builder().stringValue(value).dataType("String").build()
            ));
        }

        sqsClient.sendMessage(builder.build());
        log.info("Message with attributes sent to queue {}: {}", queueUrl, messageBody);
    }
}
