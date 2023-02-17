package com.amigoscode.fraud;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("api/v1/fraud-check")
public class FraudController {

    private final FraudCheckService fraudCheckService;

     @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFrauster(@PathVariable ("customerId") Integer customerId){
         boolean isFraudulentCustomer = fraudCheckService.isFraudulentCustomer(customerId);
         return new FraudCheckResponse(isFraudulentCustomer);
     }

}
