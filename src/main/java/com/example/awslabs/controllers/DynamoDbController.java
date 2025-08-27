package com.example.awslabs.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.awslabs.dto.DynamoRecordDTO;
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
	
	@GetMapping("/dynamo/item/{tableName}/{keyName}/{keyValue}")
	public ResponseEntity<DynamoRecordDTO > getItem(@PathVariable("tableName") String tableName,
														        @PathVariable("keyName") String keyName,
														        @PathVariable("keyValue") String keyValue)
	{
		log.info("Received getItem request for table={}, key={}={}", tableName, keyName, keyValue);
		return ResponseEntity.ok(dynamoDbService.getItem(tableName, keyName, keyValue));
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
	
	@GetMapping("/query")
	public ResponseEntity<List<Map<String,AttributeValue>>> queryItems(@RequestParam("tableName") String tableName, 
			 														   @RequestParam("id") String id)
	{
		return ResponseEntity.ok(dynamoDbService.queryById(tableName, id));
	}
	
	@GetMapping("/update")
	public ResponseEntity<String> updateItem(@RequestParam("tableName") String tableName, 
			 								 @RequestParam("id") String id,
			 								 @RequestParam("newValue") String newValue)
	{
		dynamoDbService.updateItem(tableName, id, newValue);
		return ResponseEntity.ok("Item updated successfully");
	}
	
	@GetMapping("dynamo/scan/{tableName}")
	public ResponseEntity<List<DynamoRecordDTO>> scanTable(@PathVariable("tableName") String tableName)
	{
	    log.info("Received scan request for table={}", tableName);
	    List<DynamoRecordDTO> records = dynamoDbService.scanTable(tableName);
	    return ResponseEntity.ok(records);
	}
}
