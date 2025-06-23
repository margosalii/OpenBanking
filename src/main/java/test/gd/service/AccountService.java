package test.gd.service;

import test.gd.dto.AccountResponseDto;
import test.gd.model.Payment;

import java.util.List;

public interface AccountService {
    List<AccountResponseDto> getUserAccounts(Long id);

    double getBalance(String iban);
    double mockBalance(String iban);
}
