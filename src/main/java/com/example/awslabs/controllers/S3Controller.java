package com.example.awslabs.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.awslabs.service.S3Service;

import jakarta.servlet.http.HttpServletResponse;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/s3")
public class S3Controller {
	
	private final S3Service s3Service;

	public S3Controller(S3Service s3Service) {
		this.s3Service = s3Service;
	}
	
	@GetMapping("/buckets")
	public String listBuckets()
	{
	    return s3Service.listBuckets();
	}
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("bucketName") String bucketName, 
							@RequestParam("file") MultipartFile file)  throws S3Exception, AwsServiceException, SdkClientException, IOException {
		
		log.info("Received upload request for file '{}' to bucket '{}'", file.getOriginalFilename(), bucketName);
		s3Service.uploadFile(bucketName, file);
		log.info("Upload request completed for file '{}'", file.getOriginalFilename());
		return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
	}
	
	@GetMapping("/download")
	public ResponseEntity<?> downloadFile(@RequestParam("bucketName") String bucketName,
			@RequestParam("key") String key){

		key = key.trim();
		log.info("Download request for file '{}' from bucket '{}'", key, bucketName);
		byte []fileData = s3Service.downloadFile(bucketName, key);
		log.info("File '{}' downloaded successfully", key);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(fileData);
	}
	
	@GetMapping("/objects")
	public ResponseEntity<List<String>> listObjects(@RequestParam("bucketName") String bucketName)
	{
		 log.info("Listing objects in bucket '{}'", bucketName);
		 List<String> bucketObjs = s3Service.listObjects(bucketName);
		 log.info("Found {} objects in bucket '{}'", bucketObjs.size(), bucketName);
		 return ResponseEntity.ok(bucketObjs);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteFile(@RequestParam("bucketName") String bucketName,
									@RequestParam("key") String key)
	{
		 log.info("Delete request for file '{}' in bucket '{}'", key, bucketName);
		 s3Service.deleteFile(bucketName, key);
		 log.info("File '{}' deleted successfully from bucket '{}'", key, bucketName);
		 return ResponseEntity.ok("File deleted successfully: " + key);
	}
	
	@GetMapping("download/stream")
	public void downloadFileStream(@RequestParam("bucketName") String bucketName,
									@RequestParam("key") String key,
									HttpServletResponse response) throws IOException
	{
		s3Service.downloadFileStream(bucketName, key, response);
	}
	
	@GetMapping("/presigned/download")
	public ResponseEntity<String> getPresignedDownloadUrl(@RequestParam("bucketName") String bucketName,
														@RequestParam("key") String key)
	{
        log.info("API request: presigned download URL for bucket={}, key={}", bucketName, key);
        return ResponseEntity.ok(s3Service.generatePresignedUrlForDownload(bucketName, key));
	}
	
	@GetMapping("/presigned/upload")
	public ResponseEntity<String> getPresignedUploadUrl(@RequestParam("bucketName") String bucketName,
														@RequestParam("key") String key)
	{
        log.info("API request: presigned download URL for bucket={}, key={}", bucketName, key);
        return ResponseEntity.ok(s3Service.generatePresignedUrlForUpload(bucketName, key));
	}
	
	//large file upload
	@PostMapping("/upload/large")
	public ResponseEntity<String> uploadLargeFile(@RequestParam("bucketName") String bucketName,
													@RequestParam("file") MultipartFile file) throws IOException
	{
		log.info("Large file upload request received: bucket={}, file={}", bucketName, file.getOriginalFilename());
        s3Service.uploadLargeFile(bucketName, file);
        return ResponseEntity.ok("Large file uploaded successfully: " + file.getOriginalFilename());
	}
}
