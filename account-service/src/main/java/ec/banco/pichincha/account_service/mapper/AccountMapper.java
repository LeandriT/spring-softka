package ec.banco.pichincha.account_service.mapper;

import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.model.Account;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {
    Account toEntity(AccountRequest request);

    AccountDto toDto(Account customer);

    Account updateModel(AccountRequest request, @MappingTarget Account entity);
}
