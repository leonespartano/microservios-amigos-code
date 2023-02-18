package com.amigoscode.fraud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;


class FraudCheckServiceTest {

    private FraudCheckService underTest;

    @Mock
    private FraudCheckHistoryRepository fraudCheckHistoryRepository;

    @Captor
    ArgumentCaptor<FraudCheckHistory> captureFraudCheckHistory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new FraudCheckService(fraudCheckHistoryRepository);
    }

    @Test
    void itShouldNotReturnFraudulentCustomer() {
        //Given
        int customerId = 1;
        //When
        boolean result = underTest.isFraudulentCustomer(customerId);
        //Then
        then(fraudCheckHistoryRepository).should().save(captureFraudCheckHistory.capture());

        FraudCheckHistory fraudCheckHistoryValue = captureFraudCheckHistory.getValue();

        assertThat(fraudCheckHistoryValue.getCustomerId()).isEqualTo(customerId);
        assertThat(result).isFalse();

    }
}