package com.amigoscode.customer;

import lombok.AllArgsConstructor;
import org.mockito.ArgumentCaptor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public void registerCustomer(CustomerRegistrationRequest request){
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.save(customer);

    }
}
