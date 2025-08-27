package com.example.awslabs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Slf4j
@Service
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
		dynamoDb.putItem(putObjectRequest);
		log.info("Item inserted: {}", id);
	}

	public void deleteItem(String tableName, String id) {
		Map<String,AttributeValue> key = new HashMap<>();
		key.put("id", AttributeValue.builder().s(id).build());
		
		DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
																.tableName(tableName)
																.key(key)
																.build();
		dynamoDb.deleteItem(deleteItemRequest);
		log.info("Item deleted: {}", id);
	}
	
	public List<Map<String,AttributeValue>> queryById(String tableName, String id)
	{
		Map<String,AttributeValue> key = new HashMap<>();
		key.put(":idVal", AttributeValue.builder().s(id).build());
		
		QueryRequest queryRequest = QueryRequest.builder()
												.tableName(tableName)
												.keyConditionExpression("id := idVal")
												.expressionAttributeValues(key)
												.build();
		return dynamoDb.query(queryRequest).items();
		
	}

	public void updateItem(String tableName, String id, String newValue)
	{
		Map<String,AttributeValue> key = new HashMap<>();
		key.put("id", AttributeValue.builder().s(id).build());
		
		Map<String, String> expressionAttributeNames = new HashMap<>();
		expressionAttributeNames.put("#val", "value"); //value is a reserved word in update expression
		
	    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
	    expressionAttributeValues.put(":newValue", AttributeValue.builder().s(newValue).build());
	    
	    UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
	    														.tableName(tableName)
	    														.key(key)
	    														.updateExpression("SET #val = :newValue")
	    														.expressionAttributeNames(expressionAttributeNames)
	    														.expressionAttributeValues(expressionAttributeValues)
	    														.build();
	    dynamoDb.updateItem(updateItemRequest);
	    log.info("Item with id={} updated in table={} with value={}", id, tableName, newValue);
	}
}
