package rewards;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbcBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbcBootApplication.class, args);
    }

    /*
    This bean will be run after the initialization of the Application Context.

    Functional interface used to indicate that a bean should run when it is contained within a SpringApplication.
    Multiple CommandLineRunner beans can be defined within the same application context and can be ordered using the
    Ordered interface or @Order annotation. If you need access to ApplicationArguments instead of the raw String
    array consider using ApplicationRunner.
     */
    @Bean
    CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate) {

        String QUERY = "SELECT count(*) FROM T_ACCOUNT";

        // Use Lambda expression to display the result
        return args -> System.out.println("Hello, there are "
                + jdbcTemplate.queryForObject(QUERY, Long.class)
                + " accounts");
    }

}
