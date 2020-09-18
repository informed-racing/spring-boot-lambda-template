package informed.racing;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import informed.racing.services.ExampleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
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
