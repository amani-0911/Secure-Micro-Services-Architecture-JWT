package org.sid.compteservice;

import org.sid.compteservice.entities.Compte;
import org.sid.compteservice.repository.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.Random;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class CompteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompteServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(CompteRepository compteRepository){
        String[] types={"courant","epargne"};
        return args -> {
            for (int i = 0; i <10 ; i++) {
                String type=types[new Random().nextInt(1)];
                compteRepository.save(new
                        Compte(null,type,Math.random()*9000));
            }
        };
    }


}
