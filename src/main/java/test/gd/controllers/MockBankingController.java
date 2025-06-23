package test.gd.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.gd.model.Account;
import test.gd.model.Payment;
import test.gd.model.PaymentStatus;
import test.gd.service.IbanGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Tag(name = "Mock account balance and transactions")
@RestController
@RequestMapping("/mock-api/accounts")
public class MockBankingController {
    @GetMapping("/{iban}/balance")
    public double getMockedBalance(@PathVariable String iban) {
        return new Random().nextDouble(2000);
    }

    @GetMapping("/{iban}/transactions")
    public List<Payment> getMockedPayments(@PathVariable String iban) {
        Account account = new Account();
        account.setIban(iban);

        List<Payment> paymentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Payment payment = new Payment();
            payment.setAmount(new Random().nextDouble(200));
            payment.setSenderIban(account);
            payment.setReceiverIban(IbanGenerator.generateRandomUAIban());
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setDateTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));
            paymentList.add(payment);
        }
        return paymentList;
    }
}
