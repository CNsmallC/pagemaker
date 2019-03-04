package cn.smallc.pagemaker;

import cn.smallc.pagemaker.main.MakePage_Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@SpringBootConfiguration
//@ComponentScan(basePackageClasses = {})
public class PagemakerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PagemakerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        MakePage_Main.main(args);
    }
}
