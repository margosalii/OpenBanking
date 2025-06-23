package test.gd.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import test.gd.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import test.gd.dto.AccountResponseDto;
import test.gd.dto.PaymentResponseDto;
import test.gd.model.User;
import test.gd.repository.UserRepository;
import test.gd.service.AccountService;
import test.gd.service.PaymentService;
import java.util.List;

@Tag(name = "Account info",
    description = "Endpoints for getting account information, its balance and last 10 transactions")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @GetMapping
    public List<AccountResponseDto> getUserAccounts(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(
            () -> new EntityNotFoundException("Can't find user with email: " + email)
        );
        return accountService.getUserAccounts(user.getId());
    }

    @GetMapping("/{iban}/balance")
    public double getAccountBalance(@PathVariable String iban) {
        return accountService.getBalance(iban);
    }

    @GetMapping("/{iban}/transactions")
    public List<PaymentResponseDto> getLast10Payments(@PathVariable String iban) {
        return paymentService.get10LastPayments(iban);
    }
}
