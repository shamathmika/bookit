package com._2.BookIt.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLConnection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
	@Value ("${aws.s3.bucket}")
	private String bucketName;
	
	@Value ("${aws.accessKey}")
	private String accessKey;
	
	@Value ("${aws.secretKey}")
	private String secretKey;
	
	@Value ("${aws.region}")
	private String region;
	
	public List<String> uploadImages (List<MultipartFile> images) throws IOException {
		S3Client s3 = S3Client.builder()
				.region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(
						AwsBasicCredentials.create(accessKey, secretKey)))
				.build();
		
		List<String> urls = new ArrayList<>();
		for (MultipartFile file : images) {
			try {
				String filename = "restaurant/" + Instant.now().getEpochSecond() + "-" + file.getOriginalFilename();
				s3.putObject(
						PutObjectRequest.builder()
								.bucket(bucketName)
								.key(filename)
								.contentType(file.getContentType())
								.build(),
						RequestBody.fromBytes(file.getBytes())
				);
				
				String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + filename;
				urls.add(fileUrl);
				
			} catch (IOException e) {
				throw new RuntimeException("Failed to upload image to S3", e);
			}
		}
		return urls;
	}
}
