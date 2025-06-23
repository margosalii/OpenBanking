package test.gd.service;

import test.gd.dto.PaymentRequestDto;
import test.gd.dto.PaymentResponseDto;
import test.gd.model.Payment;

import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> get10LastPayments(String iban);
    List<Payment> mockPayments(String iban);
    PaymentResponseDto initiatePayment(Long userId, PaymentRequestDto requestDto);
}
