package exercise;

import exercise.daytime.Day;
import exercise.daytime.Daytime;
import exercise.daytime.Night;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

// BEGIN
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;
// END

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @RequestScope
    @Bean
    public Daytime getDaytime() {
        int hour = LocalDateTime.now().getHour();

        if (hour >= 6 && hour < 22) {
            return new Day();
        }
        return new Night();
    }
    // END
}
