package ec.banco.pichincha.account_service.mapper;

import ec.banco.pichincha.account_service.dto.transaction.v1.request.TransactionRequest;
import ec.banco.pichincha.account_service.dto.transaction.v1.response.TransactionDto;
import ec.banco.pichincha.account_service.model.Transaction;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransactionMapper {
    Transaction toEntity(TransactionRequest request);

    TransactionDto toDto(Transaction entity);

    Transaction updateModel(TransactionRequest request, @MappingTarget Transaction entity);
}
