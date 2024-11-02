package tn.esprit.spring;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.Services.Bloc.BlocService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FoyerApplicationTests {

    @Autowired
    BlocService blocService;

    @Test
    void testGreetingService() {
        // Teste que le service de salutation retourne le bon message
        String greeting = blocService.greet();
        System.out.println("Message de salutation : " + greeting); // Affiche le message dans la console
        assertThat(greeting).isEqualTo("Hello, World!");
    }
}
