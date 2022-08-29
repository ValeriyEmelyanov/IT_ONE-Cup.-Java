package ru.vk.competition.minbenchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.vk.competition.minbenchmark.controller.TableManagerController;

@SpringBootApplication
public class MinbenchmarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinbenchmarkApplication.class, args);
    }

}
