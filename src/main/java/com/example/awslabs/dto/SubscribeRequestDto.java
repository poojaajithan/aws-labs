package com.example.awslabs.dto;

import lombok.Data;

@Data
public class SubscribeRequestDto {
    private String topicArn;
    private String protocol;
    private String endpoint;
}
