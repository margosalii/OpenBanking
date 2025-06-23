package test.gd.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import test.gd.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.gd.dto.PaymentRequestDto;
import test.gd.dto.PaymentResponseDto;
import test.gd.model.User;
import test.gd.repository.UserRepository;
import test.gd.service.PaymentService;

@Tag(name = "Controller for payments", description = "Endpoint for initiating payments")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @PostMapping("/initiate")
    public PaymentResponseDto initiatePayment(@AuthenticationPrincipal Jwt jwt,
                                              @RequestBody PaymentRequestDto requestDto) {
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(
            () -> new EntityNotFoundException("Can't find user with email: " + email)
        );
        return paymentService.initiatePayment(user.getId(), requestDto);
    }
}
