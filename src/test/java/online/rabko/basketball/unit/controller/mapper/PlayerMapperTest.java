package online.rabko.basketball.unit.controller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import online.rabko.basketball.controller.mapper.PlayerMapper;
import online.rabko.basketball.entity.Player;
import online.rabko.basketball.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Unit tests for {@link PlayerMapper}.
 */
class PlayerMapperTest {

    private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

    private Team team;

    @BeforeEach
    void setUp() {
        team = Team.builder().id(10L).build();
    }

    @Test
    void toDto_shouldMapAllFields() {
        Player src = Player.builder()
            .id(100L)
            .team(team)
            .firstName("John")
            .lastName("Doe")
            .position("Guard")
            .age(25)
            .height(190)
            .weight(85)
            .build();

        online.rabko.model.Player dto = mapper.toDto(src);

        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getTeamId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Guard", dto.getPosition());
        assertEquals(25, dto.getAge());
        assertEquals(190, dto.getHeight());
        assertEquals(85, dto.getWeight());
    }

    @Test
    void toDto_shouldHandleNullTeam() {
        Player src = Player.builder()
            .id(101L)
            .team(null)
            .firstName("Mike")
            .lastName("Smith")
            .position("Center")
            .age(30)
            .height(210)
            .weight(110)
            .build();

        online.rabko.model.Player dto = mapper.toDto(src);

        assertEquals(101L, dto.getId());
        assertNull(dto.getTeamId());
        assertEquals("Mike", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("Center", dto.getPosition());
        assertEquals(30, dto.getAge());
        assertEquals(210, dto.getHeight());
        assertEquals(110, dto.getWeight());
    }

    @Test
    void toEntity_shouldMapScalars_andWrapTeamId() {
        online.rabko.model.Player dto = new online.rabko.model.Player()
            .id(999L)
            .teamId(20L)
            .firstName("Alex")
            .lastName("Brown")
            .position("Forward")
            .age(22)
            .height(200)
            .weight(90);

        Player entity = mapper.toEntity(dto);

        assertEquals("Alex", entity.getFirstName());
        assertEquals("Brown", entity.getLastName());
        assertEquals("Forward", entity.getPosition());
        assertEquals(22, entity.getAge());
        assertEquals(200, entity.getHeight());
        assertEquals(90, entity.getWeight());

        assertNotNull(entity.getTeam());
        assertEquals(20L, entity.getTeam().getId());
    }

    @Test
    void toEntity_shouldLeaveTeamNull_whenIdNull() {
        online.rabko.model.Player dto = new online.rabko.model.Player()
            .firstName("Sam")
            .lastName("Green")
            .position("Guard")
            .age(19)
            .height(185)
            .weight(75);

        Player entity = mapper.toEntity(dto);

        assertEquals("Sam", entity.getFirstName());
        assertEquals("Green", entity.getLastName());
        assertEquals("Guard", entity.getPosition());
        assertEquals(19, entity.getAge());
        assertEquals(185, entity.getHeight());
        assertEquals(75, entity.getWeight());

        assertNotNull(entity.getTeam());
        assertNull(entity.getTeam().getId());
    }
}
