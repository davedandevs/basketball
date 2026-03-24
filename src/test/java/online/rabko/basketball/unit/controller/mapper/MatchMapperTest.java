package online.rabko.basketball.unit.controller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import online.rabko.basketball.controller.mapper.MatchMapper;
import online.rabko.basketball.entity.Match;
import online.rabko.basketball.entity.Season;
import online.rabko.basketball.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Unit tests for {@link MatchMapper}.
 */
class MatchMapperTest {

    private final MatchMapper mapper = Mappers.getMapper(MatchMapper.class);

    private Season season;
    private Team home;
    private Team away;

    @BeforeEach
    void setUp() {
        season = Season.builder().id(1L).build();
        home = Team.builder().id(10L).build();
        away = Team.builder().id(20L).build();
    }

    @Test
    void toDto_shouldMapAllFields() {
        LocalDate date = LocalDate.of(2025, 1, 2);
        Match src = Match.builder()
            .id(100L)
            .season(season)
            .date(date)
            .homeTeam(home)
            .awayTeam(away)
            .homeTeamScore(88)
            .awayTeamScore(77)
            .build();

        online.rabko.model.Match dto = mapper.toDto(src);

        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getSeasonId());
        assertEquals(date, dto.getDate());
        assertEquals(10L, dto.getHomeTeamId());
        assertEquals(20L, dto.getAwayTeamId());
        assertEquals(88, dto.getHomeTeamScore());
        assertEquals(77, dto.getAwayTeamScore());
    }

    @Test
    void toDto_shouldHandleNullRelations() {
        LocalDate date = LocalDate.of(2025, 2, 3);
        Match src = Match.builder()
            .id(101L)
            .season(null)
            .date(date)
            .homeTeam(null)
            .awayTeam(null)
            .homeTeamScore(50)
            .awayTeamScore(60)
            .build();

        online.rabko.model.Match dto = mapper.toDto(src);

        assertEquals(101L, dto.getId());
        assertEquals(date, dto.getDate());
        assertNull(dto.getSeasonId());
        assertNull(dto.getHomeTeamId());
        assertNull(dto.getAwayTeamId());
        assertEquals(50, dto.getHomeTeamScore());
        assertEquals(60, dto.getAwayTeamScore());
    }

    @Test
    void toEntity_shouldMapScalars_andWrapIdsIntoShallowRelations() {
        LocalDate date = LocalDate.of(2025, 3, 4);
        online.rabko.model.Match dto = new online.rabko.model.Match()
            .id(999L)
            .seasonId(1L)
            .date(date)
            .homeTeamId(10L)
            .awayTeamId(20L)
            .homeTeamScore(70)
            .awayTeamScore(71);

        Match entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals(date, entity.getDate());
        assertEquals(70, entity.getHomeTeamScore());
        assertEquals(71, entity.getAwayTeamScore());

        assertNotNull(entity.getSeason());
        assertEquals(1L, entity.getSeason().getId());

        assertNotNull(entity.getHomeTeam());
        assertEquals(10L, entity.getHomeTeam().getId());

        assertNotNull(entity.getAwayTeam());
        assertEquals(20L, entity.getAwayTeam().getId());
    }

    @Test
    void toEntity_shouldLeaveRelationsNull_whenIdsNull() {
        LocalDate date = LocalDate.of(2025, 4, 5);
        online.rabko.model.Match dto = new online.rabko.model.Match()
            .date(date)
            .homeTeamScore(10)
            .awayTeamScore(11);

        Match entity = mapper.toEntity(dto);

        assertEquals(date, entity.getDate());
        assertEquals(10, entity.getHomeTeamScore());
        assertEquals(11, entity.getAwayTeamScore());

        assertNotNull(entity.getSeason());
        assertNull(entity.getSeason().getId());

        assertNotNull(entity.getHomeTeam());
        assertNull(entity.getHomeTeam().getId());

        assertNotNull(entity.getAwayTeam());
        assertNull(entity.getAwayTeam().getId());
    }

}
