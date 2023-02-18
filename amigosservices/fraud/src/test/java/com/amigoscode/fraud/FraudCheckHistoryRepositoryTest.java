package com.amigoscode.fraud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class FraudCheckHistoryRepositoryTest {

    @Autowired
    private FraudCheckHistoryRepository fraudCheckHistoryRepository;

    @Test
    void itShouldSaveFraudCheckHistory() {
        //Given

        FraudCheckHistory test = new FraudCheckHistory(null, 1,false, LocalDateTime.now());

        //When
        FraudCheckHistory underTest = fraudCheckHistoryRepository.save(test);
        //Then
        Optional<FraudCheckHistory> optionalFraudCheckHistory = fraudCheckHistoryRepository.findById(underTest.getId());
        assertThat(optionalFraudCheckHistory)
                .isPresent()
                .hasValueSatisfying(
                        f -> {
                            assertThat(f).usingRecursiveComparison()
                                    .ignoringFields("id")
                                    .isEqualTo(test);
                        }
                );
    }
}