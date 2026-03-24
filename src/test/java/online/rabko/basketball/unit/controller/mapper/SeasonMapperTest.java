package online.rabko.basketball.unit.controller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import online.rabko.basketball.controller.mapper.SeasonMapper;
import online.rabko.basketball.entity.Season;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Unit tests for {@link SeasonMapper}.
 */
class SeasonMapperTest {

    private final SeasonMapper mapper = Mappers.getMapper(SeasonMapper.class);

    @Test
    void toDto_shouldMapAllFields() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);

        Season entity = Season.builder()
            .id(1L)
            .name("Season 2025")
            .startDate(start)
            .endDate(end)
            .build();

        online.rabko.model.Season dto = mapper.toDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Season 2025", dto.getName());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
    }

    @Test
    void toEntity_shouldMapScalars() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 6, 30);

        online.rabko.model.Season dto = new online.rabko.model.Season()
            .id(999L)
            .name("Season 2026")
            .startDate(start)
            .endDate(end);

        Season entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("Season 2026", entity.getName());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
    }
}
