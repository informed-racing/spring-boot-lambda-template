package informed.racing.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class OnBoot implements CommandLineRunner {

    @Override
    public void run(String... args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    }

}
