package br.com.judev.eventotecbackend.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.region")
    private String awsRegiom;

    @Bean
    public AmazonS3 createS3ClientInstance(){
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(awsRegiom)
                .build();
    }

}
