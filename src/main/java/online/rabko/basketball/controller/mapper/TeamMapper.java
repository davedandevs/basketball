package online.rabko.basketball.controller.mapper;

import online.rabko.basketball.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Team entity and DTO.
 */
@Mapper(config = CentralMapperConfig.class)
public interface TeamMapper {

    /**
     * Converts a Team entity to a Team DTO.
     */
    online.rabko.model.Team toDto(Team source);

    /**
     * Converts a Team DTO to a Team entity.
     */
    @Mapping(target = "id", ignore = true)
    Team toEntity(online.rabko.model.Team dto);
}
