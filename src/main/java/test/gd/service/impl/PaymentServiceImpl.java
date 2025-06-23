package test.gd.service.impl;

import test.gd.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import test.gd.dto.PaymentRequestDto;
import test.gd.dto.PaymentResponseDto;
import test.gd.mapper.PaymentMapper;
import test.gd.model.Account;
import test.gd.model.Payment;
import test.gd.model.PaymentStatus;
import test.gd.repository.AccountRepository;
import test.gd.repository.PaymentRepository;
import test.gd.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final PaymentMapper paymentMapper;
    private final AccountRepository accountRepository;

    @Override
    public List<PaymentResponseDto> get10LastPayments(String iban) {
        accountRepository.findByIban(iban).orElseThrow(
            () -> new EntityNotFoundException("Can't find user with IBAN: " + iban)
        );
        List<Payment> payments = paymentRepository
            .findTop10BySenderIbanIbanOrReceiverIbanOrderByDateTimeDesc(iban, iban);

        if (payments.isEmpty()) {
            List<Payment> mockedPayments = mockPayments(iban);
            for (Payment payment : mockedPayments) {
                paymentRepository.save(payment);
            }
            return mockedPayments.stream().map(paymentMapper::toDto).toList();
        }
        return payments.stream().map(paymentMapper::toDto).toList();
    }

    @Override
    public List<Payment> mockPayments(String iban) {
        String url = "http://localhost:8080/mock-api/accounts/" + iban + "/transactions";

        ResponseEntity<List<Payment>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Payment>>() {}
        );

        return response.getBody();
    }

    @Override
    public PaymentResponseDto initiatePayment(Long userId, PaymentRequestDto requestDto) {
        Payment payment = paymentMapper.toModel(requestDto);
        payment.setDateTime(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        double amount = requestDto.getAmount();

        Account senderAccount = accountRepository.findByIban(requestDto.getSenderIban()).orElseThrow(
            () -> new EntityNotFoundException("Can't find account with IBAN: " + requestDto.getSenderIban())
        );
        Account receiverAccount = accountRepository.findByIban(requestDto.getReceiverIban()).orElseThrow(
            () -> new EntityNotFoundException("Can't find account with IBAN: " + requestDto.getReceiverIban())
        );

        double senderBalance = senderAccount.getBalance() - amount;
        double receiverBalance = receiverAccount.getBalance() + amount;

        if (userId != senderAccount.getUser().getId() || senderBalance < 0) {
            payment.setStatus(PaymentStatus.FAILED);
            return paymentMapper.toDto(paymentRepository.save(payment));
        }

        senderAccount.setBalance(senderBalance);
        receiverAccount.setBalance(receiverBalance);
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

}
