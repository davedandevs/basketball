package online.rabko.basketball.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.rabko.api.TeamsApi;
import online.rabko.basketball.controller.mapper.TeamMapper;
import online.rabko.basketball.entity.Team;
import online.rabko.basketball.service.basketball.BasketballTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing teams.
 */
@RestController
@RequiredArgsConstructor
public class TeamsController implements TeamsApi {

    private final BasketballTeamService basketballTeamService;
    private final TeamMapper teamMapper;

    @Override
    public ResponseEntity<List<online.rabko.model.Team>> teamsGet() {
        return ResponseEntity.ok(
            basketballTeamService.findAll().stream()
                .map(teamMapper::toDto)
                .toList()
        );
    }

    @Override
    public ResponseEntity<online.rabko.model.Team> teamsIdGet(Long id) {
        return ResponseEntity.ok(
            teamMapper.toDto(basketballTeamService.findById(id))
        );
    }

    @Override
    public ResponseEntity<Void> teamsIdDelete(Long id) {
        basketballTeamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<online.rabko.model.Team> teamsIdPut(Long id,
        online.rabko.model.Team dto) {
        Team updated = basketballTeamService.update(id, teamMapper.toEntity(dto));
        return ResponseEntity.ok(teamMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<online.rabko.model.Team> teamsPost(online.rabko.model.Team dto) {
        Team created = basketballTeamService.create(teamMapper.toEntity(dto));
        return ResponseEntity.status(CREATED).body(teamMapper.toDto(created));
    }
}
