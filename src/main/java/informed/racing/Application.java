package informed.racing;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import informed.racing.services.ExampleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.function.Function;

@SpringBootApplication(scanBasePackages = "informed.racing")
@EntityScan(basePackages = "informed.racing.common")
@EnableJpaRepositories(basePackages = "informed.racing.common")
public class Application {

    private final ExampleService exampleService;

    public Application(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @Bean("ExampleHandler")
    public Function<SQSEvent, String> parserJobHandler() {
        return exampleService::handle;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
