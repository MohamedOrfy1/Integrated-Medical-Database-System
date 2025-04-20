package com.example.demo;

import org.springframework.boot.SpringApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {

        //loading enviroment variables into .env file
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("POSTGRES_PORT", dotenv.get("POSTGRES_PORT"));
        System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
        System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));



        SpringApplication.run(DemoApplication.class, args);
    }
}