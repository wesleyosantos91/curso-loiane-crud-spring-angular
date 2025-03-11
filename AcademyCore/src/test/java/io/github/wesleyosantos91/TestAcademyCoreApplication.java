package io.github.wesleyosantos91;

import org.springframework.boot.SpringApplication;

public class TestAcademyCoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(AcademyCoreApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
