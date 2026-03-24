package online.rabko.basketball.controller.mapper;

import online.rabko.basketball.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * MapStruct mapper for converting between Match entity and DTO. Uses expression-based lookups to
 * resolve relations from IDs.
 */
@Mapper(config = CentralMapperConfig.class)
public interface MatchMapper {

    /**
     * Converts a Match entity to a Match DTO.
     */
    @Mappings({
        @Mapping(target = "seasonId", source = "season.id"),
        @Mapping(target = "homeTeamId", source = "homeTeam.id"),
        @Mapping(target = "awayTeamId", source = "awayTeam.id")
    })
    online.rabko.model.Match toDto(Match source);

    /**
     * Converts a Match DTO to a Match entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "season.id", source = "seasonId")
    @Mapping(target = "homeTeam.id", source = "homeTeamId")
    @Mapping(target = "awayTeam.id", source = "awayTeamId")
    Match toEntity(online.rabko.model.Match dto);
}

