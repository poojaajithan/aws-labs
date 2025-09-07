package com.example.awslabs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;

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
}
