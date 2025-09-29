package com.example.awslabs.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class STSServiceTest {

    @Test
    void testAssumeRole() {
        // Arrange
        StsClient mockStsClient = mock(StsClient.class);
        Credentials mockCredentials = Credentials.builder()
                .accessKeyId("testAccessKeyId")
                .secretAccessKey("testSecretAccessKey")
                .sessionToken("testSessionToken")
                .expiration(java.time.Instant.now())
                .build();
        AssumeRoleResponse mockResponse = AssumeRoleResponse.builder()
                .credentials(mockCredentials)
                .build();

        when(mockStsClient.assumeRole(any(AssumeRoleRequest.class))).thenReturn(mockResponse);

        STSService stsService = new STSService(mockStsClient);

        // Act
        stsService.assumeRole("arn:aws:iam::123456789012:role/test-role", "test-session");

        // Assert
        verify(mockStsClient, times(1)).assumeRole(any(AssumeRoleRequest.class));
    }
}
