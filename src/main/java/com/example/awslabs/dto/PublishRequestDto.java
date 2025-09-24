package com.example.awslabs.dto;

import lombok.Data;

@Data
public class PublishRequestDto {
    private String topicArn;
    private String message;
}
