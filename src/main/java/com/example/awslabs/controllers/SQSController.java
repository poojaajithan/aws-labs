package com.example.awslabs.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/list")
	public ResponseEntity<List<String>> listQueues()
	{
		log.info("API request → list SQS queues");
		List<String> queueUrls = sqsService.listQueues();
		return ResponseEntity.ok(queueUrls);
	}

	@PostMapping("/create")
	public ResponseEntity<String> createQueue(@RequestParam("queueName") String queueName) {
		log.info("API request → create SQS queue: {}", queueName);
		String queueUrl = sqsService.createQueue(queueName);
		return ResponseEntity.ok("Queue created: " + queueUrl);
	}
	
	@PostMapping("/delete")
    public ResponseEntity<String> deleteQueue(@RequestParam("queueUrl") String queueUrl) {
        log.info("API request → delete SQS queue: {}", queueUrl);
        sqsService.deleteQueue(queueUrl);
        return ResponseEntity.ok("Queue deleted: " + queueUrl);
    }

    @PostMapping("/sendWithAttributes")
    public ResponseEntity<String> sendMessageWithAttributes(
            @RequestParam("queueUrl") String queueUrl,
            @RequestParam("message") String message) {

        log.info("API request → send message with attributes to SQS");
        sqsService.sendMessageWithCustomAttributes(queueUrl, message);
        return ResponseEntity.ok("Message with attributes sent successfully: " + message);
    }
	
}