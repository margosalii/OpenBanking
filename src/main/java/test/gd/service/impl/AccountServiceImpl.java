package test.gd.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import test.gd.dto.AccountResponseDto;
import test.gd.exceptions.EntityNotFoundException;
import test.gd.mapper.AccountMapper;
import test.gd.model.Account;
import test.gd.model.Payment;
import test.gd.model.User;
import test.gd.repository.AccountRepository;
import test.gd.repository.UserRepository;
import test.gd.service.AccountService;
import test.gd.service.IbanGenerator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Override
    public List<AccountResponseDto> getUserAccounts(Long id) {
        List<AccountResponseDto> accounts = accountRepository.findAllByUserId(id)
            .stream()
            .map(accountMapper::toDto)
            .collect(Collectors.toList());

        if (accounts.isEmpty()) {
            Account account = new Account();
            User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by ID: " + id)
            );
            account.setUser(user);
            account.setIban(IbanGenerator.generateRandomUAIban());
            account.setBalance(mockBalance(account.getIban()));
            accountRepository.save(account);
            accounts.add(accountMapper.toDto(account));
        }
        return accounts;
    }

    @Override
    public double getBalance(String iban) {
        return accountRepository.findByIban(iban).orElseThrow(
            () -> new EntityNotFoundException("Can't find user with IBAN: " + iban)
        ).getBalance();
    }

    @Override
    public double mockBalance(String iban) {
        String url = "http://localhost:8080/mock-api/accounts/" + iban + "/balance";
        return restTemplate.getForObject(url, Double.class);
    }
}
