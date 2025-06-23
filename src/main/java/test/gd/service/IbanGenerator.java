package test.gd.service;

import java.math.BigInteger;
import java.util.Random;

public class IbanGenerator {
    public static String generateRandomUAIban() {
        String countryCode = "UA";
        String checkDigits = String.format("%02d", new Random().nextInt(100));

        String bankCode = String.format("%06d", new Random().nextInt(1_000_000));
        String accountNumber = String.format("%019d", new BigInteger(63, new Random()));

        return countryCode + checkDigits + bankCode + accountNumber;
    }
}
