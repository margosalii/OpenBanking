package test.gd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponseDto {
    private String iban;
    private double balance;
}
