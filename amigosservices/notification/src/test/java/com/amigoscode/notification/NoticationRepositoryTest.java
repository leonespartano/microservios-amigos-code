package com.amigoscode.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class NoticationRepositoryTest {

    @Autowired
    private NoticationRepository underTest;


    @Test
    void itShouldSaveNotification() {
        //Given
        int notificationId = 1;
        Notification notificationTest = Notification.builder()
                .notificationId(notificationId)
                .toCustomerId(1)
                .toCustomerEmail("prueba@test.com")
                .sender("Juan Perez")
                .message("Hello World")
                .build();


        //When
        underTest.save(notificationTest);
        //Then
        Optional<Notification> notificationOptional = underTest.findById(notificationId);

        assertThat(notificationOptional)
                .isPresent()
                .hasValueSatisfying(
                        n -> {
                            assertThat(n).isEqualTo(notificationTest);
                        }
                );
    }
}