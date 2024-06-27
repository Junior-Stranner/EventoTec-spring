package br.com.judev.eventotecbackend.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    // Injeta o valor da propriedade 'aws.region' do arquivo de configuração na variável 'awsRegion'
    @Value("${aws.region}")
    private String awsRegion;

 /*   @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretKey}")
    private String awsSecretKey;*/

    // Define um bean que cria e configura uma instância do cliente AmazonS3
    @Bean
    public AmazonS3 createS3ClientInstance() {
        // Cria um cliente AmazonS3 usando o AmazonS3ClientBuilder
        return AmazonS3ClientBuilder
                .standard()       // Configura o cliente usando as configurações padrão
                .withCredentials(new DefaultAWSCredentialsProviderChain()) // Define o provedor de credenciais padrão
                .withRegion(awsRegion) // Define a região AWS a partir da propriedade 'aws.region'
                .build();            // Constrói e retorna a instância do cliente AmazonS3
    }
}
