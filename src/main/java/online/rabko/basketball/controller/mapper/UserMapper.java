package online.rabko.basketball.controller.mapper;

import online.rabko.basketball.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between User entity and DTO. No business logic — only field
 * mapping.
 */
@Mapper(config = CentralMapperConfig.class)
public interface UserMapper {

    /**
     * Converts a User entity to a User DTO.
     */
    online.rabko.model.User toDto(User source);

    /**
     * Converts a User DTO to a User entity.
     */
    @Mapping(target = "id", ignore = true)
    User toEntity(online.rabko.model.User dto);
}
