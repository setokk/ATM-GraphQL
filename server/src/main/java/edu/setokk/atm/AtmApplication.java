package edu.setokk.atm;

import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtmApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtmApplication.class, args);
    }

}
