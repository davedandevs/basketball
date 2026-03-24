package online.rabko.basketball.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.rabko.api.SeasonsApi;
import online.rabko.basketball.controller.mapper.SeasonMapper;
import online.rabko.basketball.entity.Season;
import online.rabko.basketball.service.basketball.BasketballSeasonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing seasons.
 */
@RestController
@RequiredArgsConstructor
public class SeasonsController implements SeasonsApi {

    private final BasketballSeasonService basketballSeasonService;
    private final SeasonMapper seasonMapper;

    @Override
    public ResponseEntity<List<online.rabko.model.Season>> seasonsGet() {
        return ResponseEntity.ok(
            basketballSeasonService.findAll().stream()
                .map(seasonMapper::toDto)
                .toList()
        );
    }

    @Override
    public ResponseEntity<online.rabko.model.Season> seasonsIdGet(Long id) {
        return ResponseEntity.ok(
            seasonMapper.toDto(basketballSeasonService.findById(id))
        );
    }

    @Override
    public ResponseEntity<Void> seasonsIdDelete(Long id) {
        basketballSeasonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<online.rabko.model.Season> seasonsIdPut(Long id,
        online.rabko.model.Season dto
    ) {
        Season updated = basketballSeasonService.update(id, seasonMapper.toEntity(dto));
        return ResponseEntity.ok(seasonMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<online.rabko.model.Season> seasonsPost(
        online.rabko.model.Season dto) {
        Season created = basketballSeasonService.create(seasonMapper.toEntity(dto));
        return ResponseEntity.status(CREATED).body(seasonMapper.toDto(created));
    }
}
