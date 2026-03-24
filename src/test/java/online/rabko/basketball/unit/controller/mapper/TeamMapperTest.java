package online.rabko.basketball.unit.controller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import online.rabko.basketball.controller.mapper.TeamMapper;
import online.rabko.basketball.entity.Team;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Unit tests for {@link TeamMapper}.
 */
class TeamMapperTest {

    private final TeamMapper mapper = Mappers.getMapper(TeamMapper.class);

    @Test
    void toDto_shouldMapAllFields() {
        Team entity = Team.builder()
            .id(1L)
            .name("Lakers")
            .build();

        online.rabko.model.Team dto = mapper.toDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Lakers", dto.getName());
    }

    @Test
    void toEntity_shouldMapScalars() {
        online.rabko.model.Team dto = new online.rabko.model.Team()
            .id(999L)
            .name("Warriors");

        Team entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("Warriors", entity.getName());
    }
}
