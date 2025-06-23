package test.gd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import test.gd.config.MapperConfig;
import test.gd.dto.PaymentRequestDto;
import test.gd.dto.PaymentResponseDto;
import test.gd.model.Account;
import test.gd.model.Payment;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {

    @Mapping(target = "senderIban", source = "senderIban.iban")
    PaymentResponseDto toDto(Payment payment);

    @Mapping(target = "senderIban", source = "senderIban", qualifiedByName = "mapIbanToAccount")
    Payment toModel(PaymentRequestDto requestDto);

    @Named("mapIbanToAccount")
    static Account mapIbanToAccount(String iban) {
        Account account = new Account();
        account.setIban(iban);
        return account;
    }
}
