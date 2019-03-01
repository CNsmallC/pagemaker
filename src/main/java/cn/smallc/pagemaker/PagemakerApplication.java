package cn.smallc.pagemaker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
@SpringBootConfiguration
@ComponentScan
public class PagemakerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PagemakerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {



    }
}
