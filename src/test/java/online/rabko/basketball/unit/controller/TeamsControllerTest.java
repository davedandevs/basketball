package online.rabko.basketball.unit.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import online.rabko.basketball.controller.TeamsController;
import online.rabko.basketball.controller.mapper.TeamMapper;
import online.rabko.basketball.entity.Team;
import online.rabko.basketball.service.basketball.BasketballTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Unit tests for {@link TeamsController}.
 */
@ExtendWith(MockitoExtension.class)
class TeamsControllerTest {

    @Mock
    private BasketballTeamService basketballTeamService;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamsController teamsController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(teamsController);
    }

    @Test
    void teamsGet_shouldReturnList() {
        Team t1 = Team.builder().id(1L).name("Team A").build();
        Team t2 = Team.builder().id(2L).name("Team B").build();
        when(basketballTeamService.findAll()).thenReturn(List.of(t1, t2));
        when(teamMapper.toDto(t1)).thenReturn(new online.rabko.model.Team());
        when(teamMapper.toDto(t2)).thenReturn(new online.rabko.model.Team());

        given()
            .when()
            .get("/teams")
            .then()
            .statusCode(200)
            .body("$", hasSize(2));

        verify(basketballTeamService).findAll();
        verify(teamMapper, times(2)).toDto(any(Team.class));
    }

    @Test
    void teamsIdGet_shouldReturnTeam() {
        Long id = 42L;
        Team entity = Team.builder().id(id).name("Team X").build();
        when(basketballTeamService.findById(id)).thenReturn(entity);
        when(teamMapper.toDto(entity)).thenReturn(new online.rabko.model.Team());

        given()
            .when()
            .get("/teams/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(basketballTeamService).findById(id);
        verify(teamMapper).toDto(entity);
    }

    @Test
    void teamsPost_shouldCreateTeam() {
        Team toCreate = Team.builder().name("New Team").build();
        Team created = Team.builder().id(10L).name("New Team").build();
        when(teamMapper.toEntity(any(online.rabko.model.Team.class))).thenReturn(toCreate);
        when(basketballTeamService.create(toCreate)).thenReturn(created);
        when(teamMapper.toDto(created)).thenReturn(new online.rabko.model.Team());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .post("/teams")
            .then()
            .statusCode(201)
            .body("$", notNullValue());

        verify(teamMapper).toEntity(any(online.rabko.model.Team.class));
        verify(basketballTeamService).create(toCreate);
        verify(teamMapper).toDto(created);
    }

    @Test
    void teamsIdPut_shouldUpdateTeam() {
        Long id = 7L;
        Team toUpdate = Team.builder().name("Updated Team").build();
        Team updated = Team.builder().id(id).name("Updated Team").build();
        when(teamMapper.toEntity(any(online.rabko.model.Team.class))).thenReturn(toUpdate);
        when(basketballTeamService.update(eq(id), eq(toUpdate))).thenReturn(updated);
        when(teamMapper.toDto(updated)).thenReturn(new online.rabko.model.Team());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/teams/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(teamMapper).toEntity(any(online.rabko.model.Team.class));
        verify(basketballTeamService).update(id, toUpdate);
        verify(teamMapper).toDto(updated);
    }

    @Test
    void teamsIdDelete_shouldDeleteTeam() {
        Long id = 9L;
        doNothing().when(basketballTeamService).delete(id);

        given()
            .when()
            .delete("/teams/{id}", id.toString())
            .then()
            .statusCode(204);

        verify(basketballTeamService).delete(id);
        verifyNoInteractions(teamMapper);
    }

    @Test
    void teamsIdGet_shouldReturnNotFound() {
        Long id = 404L;
        when(basketballTeamService.findById(id))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        given()
            .when()
            .get("/teams/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void teamsIdDelete_shouldReturnConflict() {
        Long id = 1L;
        doThrow(new ResponseStatusException(HttpStatus.CONFLICT))
            .when(basketballTeamService).delete(id);

        given()
            .when()
            .delete("/teams/{id}", id.toString())
            .then()
            .statusCode(409);
    }
}
