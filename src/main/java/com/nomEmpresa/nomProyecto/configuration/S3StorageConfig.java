package com.nomEmpresa.nomProyecto.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3StorageConfig {

    @Value("${username}")
    private String username;

    @Value("${accessKey}")
    private String accesKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${region}")
    private String region;

    @Value("${serviceUrl}")
    private String serviceUrl;

    @Bean
    public AmazonS3 credentials(){
        AWSCredentials credentials = new BasicAWSCredentials(accesKey,secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceUrl,region))
                .build();
    }

//    @Bean
//    public AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder(){
//        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
//        AmazonS
//    }


}
