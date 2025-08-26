package com.example.awslabs.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.awslabs.service.DynamoDbService;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/dynamodb")
public class DynamoDbController {

	private final DynamoDbService dynamoDbService;

	public DynamoDbController(DynamoDbService dynamoDbService) {
		this.dynamoDbService = dynamoDbService;
	}
	
	@GetMapping("/get")
	public ResponseEntity<Map<String, AttributeValue>> getItem(@RequestParam("tableName") String tableName, 
						@RequestParam("id") String id)
	{
		return ResponseEntity.ok(dynamoDbService.getItem(tableName, id));
	}
	
	@PostMapping("/put")
	public ResponseEntity<String> putItem(@RequestParam("tableName") String tableName, 
						@RequestParam("id") String id,
						@RequestParam("name") String name)
	{
		dynamoDbService.putItem(tableName, id, name);
		return ResponseEntity.ok("Item inserted successfully");
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteItem(@RequestParam("tableName") String tableName, 
						@RequestParam("id") String id)
	{
		dynamoDbService.deleteItem(tableName, id);
		return ResponseEntity.ok("Item deleted successfully");
	}
}
