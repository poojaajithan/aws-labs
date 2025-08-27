package com.example.awslabs.dto;

import java.util.Map;

public class DynamoRecordDTO 
{
	private Map<String,String> attributes;

	public DynamoRecordDTO(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
