package online.rabko.basketball.controller.mapper;

import online.rabko.basketball.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * MapStruct mapper for converting between Player entity and DTO.
 */
@Mapper(config = CentralMapperConfig.class)
public interface PlayerMapper {

    /**
     * Converts a Player entity to a Player DTO.
     */
    @Mappings({
        @Mapping(target = "teamId", source = "team.id")
    })
    online.rabko.model.Player toDto(Player source);

    /**
     * Converts a Player DTO to a Player entity.
     */
    @Mapping(target = "team.id", source = "teamId")
    Player toEntity(online.rabko.model.Player dto);
}
