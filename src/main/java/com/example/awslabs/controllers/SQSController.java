package com.example.awslabs.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import com.example.awslabs.service.SQSService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sqs")
public class SQSController {
	
	private final SQSService sqsService;

	public SQSController(SQSService sqsService) {
		this.sqsService = sqsService;
	}

	@PostMapping("/send")
	public ResponseEntity<String> sendMessage(@RequestParam("queueUrl") String queueUrl,
							@RequestParam("message") String message)
	{
		log.info("API request → send message to SQS");
		sqsService.sendMessage(queueUrl, message);
		return ResponseEntity.ok("Message sent successfully : " + message);
	}
	
	@GetMapping("/receive")
	public ResponseEntity<List<String>> receiveMessages(@RequestParam("queueUrl") String queueUrl)
	{
		log.info("API request → receive messages from SQS");
		List<Message> messages = sqsService.receiveMessages(queueUrl);
		List<String> messageBodies = messages.stream().map(Message::body).collect(Collectors.toList());
		messages.forEach(msg -> sqsService.deleteMessage(queueUrl, msg.receiptHandle()));
		
		return ResponseEntity.ok(messageBodies);
	}
	
	
}
