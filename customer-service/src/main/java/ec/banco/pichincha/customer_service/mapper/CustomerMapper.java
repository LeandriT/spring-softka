package ec.banco.pichincha.customer_service.mapper;

import ec.banco.pichincha.customer_service.dto.customer.v1.request.CustomerRequest;
import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import ec.banco.pichincha.customer_service.model.Customer;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {
    @Mappings({
            @Mapping(source = "name", target = "person.name"),
            @Mapping(source = "gender", target = "person.gender"),
            @Mapping(source = "age", target = "person.age"),
            @Mapping(source = "identification", target = "person.identification"),
            @Mapping(source = "address", target = "person.address"),
            @Mapping(source = "phone", target = "person.phone")
    })
    Customer toEntity(CustomerRequest request);

    @Mappings({
            @Mapping(source = "person.name", target = "name"),
            @Mapping(source = "person.gender", target = "gender"),
            @Mapping(source = "person.age", target = "age"),
            @Mapping(source = "person.identification", target = "identification"),
            @Mapping(source = "person.address", target = "address"),
            @Mapping(source = "person.phone", target = "phone")
    })
    CustomerDto toDto(Customer customer);

    @Mappings({
            @Mapping(source = "name", target = "person.name"),
            @Mapping(source = "gender", target = "person.gender"),
            @Mapping(source = "age", target = "person.age"),
            @Mapping(source = "identification", target = "person.identification"),
            @Mapping(source = "address", target = "person.address"),
            @Mapping(source = "phone", target = "person.phone")
    })
    Customer updateModel(CustomerRequest request, @MappingTarget Customer customer);
}
