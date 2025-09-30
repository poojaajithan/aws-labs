package com.example.awslabs.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

public class S3LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
    private final S3Client s3 = S3Client.create();
    private final String bucketName = "s3trigger-lambda-bucket";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String method = event.getHttpMethod();
        System.out.println("HTTP Method: " + method);
        String response = "";

        if ("POST".equalsIgnoreCase(method) && event.getPath().equals("/upload")) {
            String fileName = event.getQueryStringParameters().get("filename");
            String body = event.getBody();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromString(body));
            response = "File uploaded successfully: " + fileName;
        } 
        else if ("GET".equalsIgnoreCase(method) && event.getPath().equals("/files/")) {
            String fileName = event.getQueryStringParameters().get("filename");

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            String fileContent = s3.getObjectAsBytes(getObjectRequest).asUtf8String();
            response = fileContent;
        } 
        else {
            response = "Unsupported operation";
        }
        return new APIGatewayProxyResponseEvent()   
                .withStatusCode(200)
                .withBody(response);    
    }
}
