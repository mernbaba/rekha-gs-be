package com.rekha.ecommerce.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class DigitalOceanSpacesConfig {

	@Value("${do.bucket.access-key}")
	private String accessKey;

	@Value("${do.bucket.secret-key}")
	private String secretKey;

	@Value("${do.bucket.endpoint}")
	private String endpoint;

	@Value("${do.bucket.region}")
	private String region;

	@Bean
	S3Client s3Client() {
		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
		return S3Client.builder().region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.endpointOverride(URI.create(endpoint))
				.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build()).build();
	}

}
