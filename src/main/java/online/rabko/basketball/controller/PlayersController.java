package online.rabko.basketball.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.rabko.api.PlayersApi;
import online.rabko.basketball.controller.mapper.PlayerMapper;
import online.rabko.basketball.entity.Player;
import online.rabko.basketball.service.basketball.BasketballPlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for {@code /players} endpoints. Implements {@link PlayersApi}.
 */
@RestController
@RequiredArgsConstructor
public class PlayersController implements PlayersApi {

    private final BasketballPlayerService basketballPlayerService;
    private final PlayerMapper playerMapper;

    @Override
    public ResponseEntity<List<online.rabko.model.Player>> playersGet() {
        return ResponseEntity.ok(
            basketballPlayerService.findAll().stream()
                .map(playerMapper::toDto)
                .toList()
        );
    }

    @Override
    public ResponseEntity<online.rabko.model.Player> playersIdGet(Long id) {
        return ResponseEntity.ok(
            playerMapper.toDto(basketballPlayerService.findById(id))
        );
    }

    @Override
    public ResponseEntity<online.rabko.model.Player> playersPost(online.rabko.model.Player dto) {
        Player created = basketballPlayerService.create(playerMapper.toEntity(dto));
        return ResponseEntity.status(CREATED).body(playerMapper.toDto(created));
    }

    @Override
    public ResponseEntity<online.rabko.model.Player> playersIdPut(Long id,
        online.rabko.model.Player dto) {
        Player updated = basketballPlayerService.update(id, playerMapper.toEntity(dto));
        return ResponseEntity.ok(playerMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<Void> playersIdDelete(Long id) {
        basketballPlayerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
