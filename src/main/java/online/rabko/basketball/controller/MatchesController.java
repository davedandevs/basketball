package online.rabko.basketball.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.rabko.api.MatchesApi;
import online.rabko.basketball.controller.mapper.MatchMapper;
import online.rabko.basketball.entity.Match;
import online.rabko.basketball.service.basketball.BasketballMatchService;
import online.rabko.model.PlayerStats;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing matches.
 */
@RestController
@RequiredArgsConstructor
public class MatchesController implements MatchesApi {

    private final BasketballMatchService basketballMatchService;
    private final MatchMapper matchMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<List<online.rabko.model.Match>> matchesGet() {
        return ResponseEntity.ok(
            basketballMatchService.findAll().stream()
                .map(matchMapper::toDto)
                .toList()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<online.rabko.model.Match> matchesIdGet(Long id) {
        return ResponseEntity.ok(
            matchMapper.toDto(basketballMatchService.findById(id))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> matchesIdDelete(Long id) {
        basketballMatchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<online.rabko.model.Match> matchesPost(online.rabko.model.Match dto) {
        Match created = basketballMatchService.create(matchMapper.toEntity(dto));
        return ResponseEntity.status(CREATED).body(matchMapper.toDto(created));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<online.rabko.model.Match> matchesIdPut(Long id,
        online.rabko.model.Match dto) {
        Match updated = basketballMatchService.update(id, matchMapper.toEntity(dto));
        return ResponseEntity.ok(matchMapper.toDto(updated));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<List<PlayerStats>> matchesMatchIdStatsGet(Long matchId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<PlayerStats> matchesMatchIdStatsPost(Long matchId,
        PlayerStats playerStats) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }
}
