package com.example.awslabs.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

@Service
@Slf4j
public class STSService {
    private final StsClient stsClient;

    // Existing default constructor
    public STSService() {
        this.stsClient = StsClient.create();
    }

    // Add this constructor for testing
    public STSService(StsClient stsClient) {
        this.stsClient = stsClient;
    }

    public void assumeRole(String roleArn, String sessionName) {
        AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName(sessionName)
                .durationSeconds(900) // 15 minutes
                .build();

        AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
        Credentials creds = roleResponse.credentials();
        log.info("Temporary Credentials:");
        log.info("AccessKeyId: {}", creds.accessKeyId());
        log.info("SecretAccessKey: {}", creds.secretAccessKey());
        log.info("SessionToken: {}", creds.sessionToken());
        log.info("Expiration: {}", creds.expiration());

    }
}
