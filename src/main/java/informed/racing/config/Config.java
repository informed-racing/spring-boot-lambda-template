package informed.racing.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import informed.racing.common.db.DBCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class Config {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    @Value("#{environment.DB_HOSTNAME}")
    private String dbHostname;
    @Value("#{environment.DB_PORT}")
    private String dbPort;
    @Value("#{environment.DB_SECRET_ARN}")
    private String dbSecretArn;
    @Value("#{environment.DB_NAME}")
    private String dbName;

    @Bean
    public DBCredentials dbCredentials(AWSSecretsManager secretsManager, ObjectMapper objectMapper) throws JsonProcessingException {
        if (activeProfile.equals("dev")) {
            return DBCredentials.builder()
                    .username("root")
                    .password("")
                    .build();
        }
        GetSecretValueResult secretValueResult = secretsManager.getSecretValue(new GetSecretValueRequest().withSecretId(dbSecretArn));
        return objectMapper.readValue(secretValueResult.getSecretString(), DBCredentials.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public AWSSecretsManager secretsManager() {
        return AWSSecretsManagerClientBuilder.defaultClient();
    }

    @Bean
    public AmazonSQS sqsClient() {
        return AmazonSQSClientBuilder.defaultClient();
    }

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.defaultClient();
    }

    @Bean
    public TransferManager s3TransferManager(AmazonS3 s3Client) {
        return TransferManagerBuilder.standard().withS3Client(s3Client).build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource(DBCredentials dbCredentials) {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://" + dbHostname + ":" + dbPort + "/" + Optional.ofNullable(dbName).orElse("informed_racing") + "?createDatabaseIfNotExist=true&serverTimezone=Europe/London")
                .username(dbCredentials.getUsername())
                .password(dbCredentials.getPassword())
                .build();
    }

}