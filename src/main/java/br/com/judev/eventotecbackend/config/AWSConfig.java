package br.com.judev.eventotecbackend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    // Injeta o valor da propriedade 'aws.region' do arquivo de configuração na variável 'awsRegion'
// 'aws.region' especifica a região da AWS onde seus recursos estão localizados, como 'us-east-1'.
    @Value("${aws.region}")
    private String awsRegion;

    // Injeta o valor da propriedade 'aws.accessKeyId' do arquivo de configuração na variável 'awsAccessKeyId'
// 'aws.accessKeyId' é a chave de acesso da AWS usada para autenticar a chamada da API.
// Esta chave identifica o usuário ou a aplicação que está realizando as solicitações.
 /*   @Value("${aws.S3accessKeyId}")
    private String awsS3AccessKey;

    // Injeta o valor da propriedade 'aws.secretKey' do arquivo de configuração na variável 'awsSecretKey'
// 'aws.secretKey' é a chave secreta da AWS usada juntamente com a chave de acesso para autenticar e autorizar a chamada da API.
// Esta chave deve ser mantida segura e nunca deve ser exposta publicamente.
    @Value("${aws.S3AccessSecretKey}")
    private String awsS3AccessSecretKey;*/


    // Define um bean que cria e configura uma instância do cliente AmazonS3
    @Bean
    public AmazonS3 createS3ClientInstance() {
     //   AWSCredentials credentials = new BasicAWSCredentials(awsS3AccessKey, awsS3AccessSecretKey);

        // Cria um cliente AmazonS3 usando o AmazonS3ClientBuilder
        return AmazonS3ClientBuilder
                .standard()       // Configura o cliente usando as configurações padrão
                .withCredentials(new DefaultAWSCredentialsProviderChain()) // Define o provedor de credenciais padrão
                .withRegion(awsRegion) // Define a região AWS a partir da propriedade 'aws.region'
                .build();            // Constrói e retorna a instância do cliente AmazonS3
    }
}
