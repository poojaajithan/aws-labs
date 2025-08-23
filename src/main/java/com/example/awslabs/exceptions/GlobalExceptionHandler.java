package com.example.awslabs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle AWS S3 specific exceptions
	@ExceptionHandler(NoSuchKeyException.class)
	public ResponseEntity<ApiError> handleS3Exception(NoSuchKeyException ex, WebRequest request)
	{
		ApiError error = new ApiError(
									HttpStatus.NOT_FOUND.value(),
									"File not found",
									ex.getMessage(),
									request.getDescription(false));
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler({ SdkClientException.class, AwsServiceException.class })
	public ResponseEntity<ApiError> handleAwsSdkErrors(Exception ex, WebRequest request)
	{
		ApiError error = new ApiError(
									HttpStatus.INTERNAL_SERVER_ERROR.value(),
									"AWS SDK Error",
									ex.getMessage(),
									request.getDescription(false));
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
    // Handle all other exceptions (generic fallback)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request)
	{
		ApiError error = new ApiError(
									HttpStatus.INTERNAL_SERVER_ERROR.value(),
									"Internal Server Error",
									ex.getMessage(),
									request.getDescription(false));
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
