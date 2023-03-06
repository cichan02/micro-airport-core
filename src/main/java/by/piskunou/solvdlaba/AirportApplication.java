package by.piskunou.solvdlaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AirportApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirportApplication.class, args);
    }

}
