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
import online.rabko.basketball.controller.MatchesController;
import online.rabko.basketball.controller.mapper.MatchMapper;
import online.rabko.basketball.entity.Match;
import online.rabko.basketball.service.basketball.BasketballMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Unit tests for {@link MatchesController}.
 */
@ExtendWith(MockitoExtension.class)
class MatchesControllerTest {

    @Mock
    private BasketballMatchService basketballMatchService;

    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchesController matchesController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(matchesController);
    }

    @Test
    void matchesGet_shouldReturnList() {
        Match m1 = Match.builder().id(1L).build();
        Match m2 = Match.builder().id(2L).build();
        when(basketballMatchService.findAll()).thenReturn(List.of(m1, m2));
        when(matchMapper.toDto(m1)).thenReturn(new online.rabko.model.Match());
        when(matchMapper.toDto(m2)).thenReturn(new online.rabko.model.Match());

        given()
            .when()
            .get("/matches")
            .then()
            .statusCode(200)
            .body("$", hasSize(2));

        verify(basketballMatchService).findAll();
        verify(matchMapper, times(2)).toDto(any(Match.class));
    }

    @Test
    void matchesIdGet_shouldReturnMatch() {
        Long id = 42L;
        Match entity = Match.builder().id(id).build();
        when(basketballMatchService.findById(id)).thenReturn(entity);
        when(matchMapper.toDto(entity)).thenReturn(new online.rabko.model.Match());

        given()
            .when()
            .get("/matches/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(basketballMatchService).findById(id);
        verify(matchMapper).toDto(entity);
    }

    @Test
    void matchesPost_shouldCreateMatch() {
        Match toCreate = Match.builder().build();
        Match created = Match.builder().id(10L).build();

        when(matchMapper.toEntity(any(online.rabko.model.Match.class))).thenReturn(toCreate);
        when(basketballMatchService.create(toCreate)).thenReturn(created);
        when(matchMapper.toDto(created)).thenReturn(new online.rabko.model.Match());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .post("/matches")
            .then()
            .statusCode(201)
            .body("$", notNullValue());

        verify(matchMapper).toEntity(any(online.rabko.model.Match.class));
        verify(basketballMatchService).create(toCreate);
        verify(matchMapper).toDto(created);
    }

    @Test
    void matchesIdPut_shouldUpdateMatch() {
        Long id = 7L;
        Match toUpdate = Match.builder().build();
        Match updated = Match.builder().id(id).build();

        when(matchMapper.toEntity(any(online.rabko.model.Match.class))).thenReturn(toUpdate);
        when(basketballMatchService.update(eq(id), eq(toUpdate))).thenReturn(updated);
        when(matchMapper.toDto(updated)).thenReturn(new online.rabko.model.Match());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/matches/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(matchMapper).toEntity(any(online.rabko.model.Match.class));
        verify(basketballMatchService).update(id, toUpdate);
        verify(matchMapper).toDto(updated);
    }

    @Test
    void matchesIdDelete_shouldDeleteMatch() {
        Long id = 9L;
        doNothing().when(basketballMatchService).delete(id);

        given()
            .when()
            .delete("/matches/{id}", id.toString())
            .then()
            .statusCode(204);

        verify(basketballMatchService).delete(id);
        verifyNoInteractions(matchMapper);
    }

    @Test
    void matchesIdGet_shouldReturnNotFound() {
        Long id = 404L;
        when(basketballMatchService.findById(id))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        given()
            .when()
            .get("/matches/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void matchesIdDelete_shouldReturnNotFound() {
        Long id = 404L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(basketballMatchService).delete(id);

        given()
            .when()
            .delete("/matches/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void matchesPost_shouldReturnConflict() {
        when(matchMapper.toEntity(any(online.rabko.model.Match.class)))
            .thenReturn(Match.builder().build());
        when(basketballMatchService.create(any(Match.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .post("/matches")
            .then()
            .statusCode(409);
    }

    @Test
    void matchesIdPut_shouldReturnBadRequest() {
        Long id = 5L;
        when(matchMapper.toEntity(any(online.rabko.model.Match.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/matches/{id}", id.toString())
            .then()
            .statusCode(400);
    }

    @Test
    void matchesIdPut_shouldReturnConflict() {
        Long id = 6L;
        when(matchMapper.toEntity(any(online.rabko.model.Match.class)))
            .thenReturn(Match.builder().build());
        when(basketballMatchService.update(eq(id), any(Match.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/matches/{id}", id.toString())
            .then()
            .statusCode(409);
    }
}
