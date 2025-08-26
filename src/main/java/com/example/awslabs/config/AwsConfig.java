package com.example.awslabs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AwsConfig {

	@Value("${aws.profile}")
	private String awsProfile;
	
	@Value("${aws.region}")
	private String awsRegion;
	
	@Bean
	public S3Client s3Client() {
        log.info("Creating S3Client for Mumbai region with default profile");

		S3Client s3 = S3Client.builder()
								.region(Region.of(awsRegion))
								.credentialsProvider(ProfileCredentialsProvider.create(awsProfile))
								.build();
		
		log.debug("S3Client created: {}", s3);
		return s3;
	}
	
	@Bean
	public DynamoDbClient dynamoDbClient() {
		log.info("Creating DynamoDbClient for Mumbai region with default profile");
		
		DynamoDbClient dynamoDb = DynamoDbClient.builder()
				.region(Region.of(awsProfile))
				.credentialsProvider(ProfileCredentialsProvider.create(awsProfile))
				.build();
		
		log.debug("DynamoDbClient created: {}", dynamoDb);
		return dynamoDb;
	}
}
