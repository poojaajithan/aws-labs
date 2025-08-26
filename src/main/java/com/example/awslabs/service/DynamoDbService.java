package com.example.awslabs.service;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoDbService {

	private final DynamoDbClient dynamoDb;
	
	public DynamoDbService(DynamoDbClient dynamoDb) {
		this.dynamoDb = dynamoDb;
	}

	public Map<String, AttributeValue> getItem(String tableName, String id) {
		Map<String, AttributeValue> key = new HashMap<>();
		key.put("id", AttributeValue.builder().s(id).build());
		
		GetItemRequest getItemRequest = GetItemRequest.builder()
														.tableName(tableName)
														.key(key)
														.build(); 
		
		return dynamoDb.getItem(getItemRequest).item();
	}
	
	public void putItem(String tableName, String id, String name)
	{
		Map<String, AttributeValue> item = new HashMap<>();
		item.put("id", AttributeValue.builder().s(id).build());
		item.put("name", AttributeValue.builder().s(name).build());
		
		PutItemRequest putObjectRequest = PutItemRequest.builder()
														.tableName(tableName)
														.item(item)
														.build();
		
	}

}
