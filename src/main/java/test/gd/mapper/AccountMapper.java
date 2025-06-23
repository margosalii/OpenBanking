package test.gd.mapper;

import org.mapstruct.Mapper;
import test.gd.config.MapperConfig;
import test.gd.dto.AccountResponseDto;
import test.gd.model.Account;

@Mapper(config = MapperConfig.class)
public interface AccountMapper {
    AccountResponseDto toDto(Account account);
}
