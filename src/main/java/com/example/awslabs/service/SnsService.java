package com.example.awslabs.service;

import org.springframework.stereotype.Service;

import com.example.awslabs.dto.CreateTopicRequestDto;
import com.example.awslabs.dto.PublishRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnsService {

    private final SnsClient snsClient;

    public CreateTopicResponse createTopic(CreateTopicRequestDto dto) {
        log.info("Creating SNS topic: {}", dto.getTopicName());
        CreateTopicRequest request = CreateTopicRequest.builder()
                .name(dto.getTopicName())
                .build();
        CreateTopicResponse response = snsClient.createTopic(request);
        log.info("SNS topic created with ARN: {}", response.topicArn());
        return response;
    }

    public PublishResponse publishMessage(PublishRequestDto dto) {
        log.info("Publishing message to topic ARN: {}", dto.getTopicArn());
        PublishRequest request = PublishRequest.builder()
                .topicArn(dto.getTopicArn())
                .message(dto.getMessage())
                .build();
        PublishResponse response = snsClient.publish(request);
        log.info("Message published with ID: {}", response.messageId());
        return response;
    }

    public SubscribeResponse subscribe(SubscribeResponseDto dto) {
        log.info("Subscribing to topic ARN: {} with protocol: {} and endpoint: {}", dto.getTopicArn(), dto.getProtocol(), dto.getEndpoint());
        SubscribeRequest request = SubscribeRequest.builder()
                .topicArn(dto.getTopicArn())
                .protocol(dto.getProtocol())
                .endpoint(dto.getEndpoint())
                .build();
        SubscribeResponse response = snsClient.subscribe(request);
        log.info("Subscription ARN: {}", response.subscriptionArn());
        return response;
    }
    
}
