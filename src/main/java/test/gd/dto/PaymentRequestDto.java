package test.gd.dto;

import lombok.Getter;
import lombok.Setter;
import test.gd.model.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentRequestDto {
    private String senderIban;
    private String receiverIban;
    private double amount;
}
