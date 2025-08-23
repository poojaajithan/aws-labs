package com.example.awslabs.service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3Service {	
	private final S3Client s3;
	
	public S3Service(S3Client s3) {
		this.s3 = s3;
	}
	
	public String listBuckets() {
		StringBuilder sb = new StringBuilder("Your S3 Buckets:\n\n");
		this.s3.listBuckets().buckets().forEach(bucket -> sb.append("- ").append(bucket.name()).append("\n"));
		return sb.toString();
	}

	public void uploadFile(String bucketName, MultipartFile file) throws S3Exception, AwsServiceException, SdkClientException, IOException {
		log.info("Starting upload for file={} to bucket={}", file.getOriginalFilename(), bucketName);

		String key = file.getOriginalFilename();
		PutObjectRequest putRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		s3.putObject(putRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

		log.debug("Upload request details: size={} bytes, contentType={}", file.getSize(), file.getContentType());
		log.info("File uploaded successfully: {}", file.getOriginalFilename());
	}

	public byte[] downloadFile(String bucketName, String key) {
		log.info("Downloading '{}' from bucket '{}'", key, bucketName);

		GetObjectRequest getRequest = GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(getRequest);
		log.info("File '{}' download complete", key);
		return objectBytes.asByteArray();
	}

	public void downloadFileStream(String bucketName, String key, HttpServletResponse response) throws IOException {
		key = key.trim();
		GetObjectRequest getRequest = GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		try(ResponseInputStream<GetObjectResponse> s3Stream = s3.getObject(getRequest);
				ServletOutputStream out = response.getOutputStream())
		{
			response.setContentType("application/octet-stream");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"");
	
			// Stream data in chunks (8KB buffer)
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = s3Stream.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
		}
	}

	public String generatePresignedUrlForDownload(String bucketName, String key) {
        log.info("Generating presigned download URL for bucket={}, key={}", bucketName, key);
        try(S3Presigner presigner = S3Presigner.create())
        {
        	GetObjectRequest getRequest = GetObjectRequest.builder()
    				.bucket(bucketName)
    				.key(key)
    				.build();
        	
        	GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        			.signatureDuration(Duration.ofMinutes(10))
        			.getObjectRequest(getRequest)
        			.build();
        	
        	String url = presigner.presignGetObject(presignRequest).url().toString();
            log.debug("Presigned download URL generated: {}", url);
            return url;
        }
	}

	public String generatePresignedUrlForUpload(String bucketName, String key) {
        log.info("Generating presigned upload URL for bucket={}, key={}", bucketName, key);
        try(S3Presigner presigner = S3Presigner.create())
        {
        	PutObjectRequest putRequest = PutObjectRequest.builder()
    				.bucket(bucketName)
    				.key(key)
    				.build();
        	
        	PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        			.signatureDuration(Duration.ofMinutes(10))
        			.putObjectRequest(putRequest)
        			.build();
        	
        	String url = presigner.presignPutObject(presignRequest).url().toString();
            log.debug("Presigned download URL generated: {}", url);
            return url;
        }
	}

	public List<String> listObjects(String bucketName) {
		ListObjectsV2Request request = ListObjectsV2Request.builder()
				.bucket(bucketName)
				.build();
		ListObjectsV2Response response = s3.listObjectsV2(request);
		
		return response.contents().stream().map(S3Object::key).collect(Collectors.toList());
	}
	
	public void deleteFile(String bucketName, String key)
	{
		DeleteObjectRequest delRequest = DeleteObjectRequest.builder()
							.bucket(bucketName)
							.key(key)
							.build();
		s3.deleteObject(delRequest);
	}
}
