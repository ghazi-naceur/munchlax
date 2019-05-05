package com.data.warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Application.class.getName());

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .logStartupInfo(false)
                .run(args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        logger.info("Munchlax started ...");
    }
}