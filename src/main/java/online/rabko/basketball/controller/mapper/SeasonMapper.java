package online.rabko.basketball.controller.mapper;

import online.rabko.basketball.entity.Season;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Season entity and DTO.
 */
@Mapper(config = CentralMapperConfig.class)
public interface SeasonMapper {

    /**
     * Converts a Season entity to a Season DTO.
     */
    online.rabko.model.Season toDto(Season source);

    /**
     * Converts a Season DTO to a Season entity.
     */
    @Mapping(target = "id", ignore = true)
    Season toEntity(online.rabko.model.Season dto);
}
