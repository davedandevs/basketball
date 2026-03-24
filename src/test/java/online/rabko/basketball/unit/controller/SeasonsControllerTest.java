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
import online.rabko.basketball.controller.SeasonsController;
import online.rabko.basketball.controller.mapper.SeasonMapper;
import online.rabko.basketball.entity.Season;
import online.rabko.basketball.service.basketball.BasketballSeasonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Unit tests for {@link SeasonsController}.
 */
@ExtendWith(MockitoExtension.class)
class SeasonsControllerTest {

    @Mock
    private BasketballSeasonService basketballSeasonService;

    @Mock
    private SeasonMapper seasonMapper;

    @InjectMocks
    private SeasonsController seasonsController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(seasonsController);
    }

    @Test
    void seasonsGet_shouldReturnList() {
        Season s1 = Season.builder().id(1L).build();
        Season s2 = Season.builder().id(2L).build();
        when(basketballSeasonService.findAll()).thenReturn(List.of(s1, s2));
        when(seasonMapper.toDto(s1)).thenReturn(new online.rabko.model.Season());
        when(seasonMapper.toDto(s2)).thenReturn(new online.rabko.model.Season());

        given()
            .when()
            .get("/seasons")
            .then()
            .statusCode(200)
            .body("$", hasSize(2));

        verify(basketballSeasonService).findAll();
        verify(seasonMapper, times(2)).toDto(any(Season.class));
    }

    @Test
    void seasonsIdGet_shouldReturnSeason() {
        Long id = 42L;
        Season entity = Season.builder().id(id).build();
        when(basketballSeasonService.findById(id)).thenReturn(entity);
        when(seasonMapper.toDto(entity)).thenReturn(new online.rabko.model.Season());

        given()
            .when()
            .get("/seasons/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(basketballSeasonService).findById(id);
        verify(seasonMapper).toDto(entity);
    }

    @Test
    void seasonsPost_shouldCreateSeason() {
        Season toCreate = Season.builder().build();
        Season created = Season.builder().id(10L).build();
        when(seasonMapper.toEntity(any(online.rabko.model.Season.class))).thenReturn(toCreate);
        when(basketballSeasonService.create(toCreate)).thenReturn(created);
        when(seasonMapper.toDto(created)).thenReturn(new online.rabko.model.Season());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .post("/seasons")
            .then()
            .statusCode(201)
            .body("$", notNullValue());

        verify(seasonMapper).toEntity(any(online.rabko.model.Season.class));
        verify(basketballSeasonService).create(toCreate);
        verify(seasonMapper).toDto(created);
    }

    @Test
    void seasonsIdPut_shouldUpdateSeason() {
        Long id = 7L;
        Season replacement = Season.builder().build();
        Season updated = Season.builder().id(id).build();
        when(seasonMapper.toEntity(any(online.rabko.model.Season.class))).thenReturn(replacement);
        when(basketballSeasonService.update(eq(id), eq(replacement))).thenReturn(updated);
        when(seasonMapper.toDto(updated)).thenReturn(new online.rabko.model.Season());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/seasons/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(seasonMapper).toEntity(any(online.rabko.model.Season.class));
        verify(basketballSeasonService).update(id, replacement);
        verify(seasonMapper).toDto(updated);
    }

    @Test
    void seasonsIdDelete_shouldDeleteSeason() {
        Long id = 9L;
        doNothing().when(basketballSeasonService).delete(id);

        given()
            .when()
            .delete("/seasons/{id}", id.toString())
            .then()
            .statusCode(204);

        verify(basketballSeasonService).delete(id);
        verifyNoInteractions(seasonMapper);
    }

    @Test
    void seasonsIdGet_shouldReturnNotFound() {
        Long id = 404L;
        when(basketballSeasonService.findById(id))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        given()
            .when()
            .get("/seasons/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void seasonsIdDelete_shouldReturnNotFound() {
        Long id = 404L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(basketballSeasonService).delete(id);

        given()
            .when()
            .delete("/seasons/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void seasonsPost_shouldReturnBadRequest() {
        when(seasonMapper.toEntity(any(online.rabko.model.Season.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .post("/seasons")
            .then()
            .statusCode(400);
    }

    @Test
    void seasonsIdPut_shouldReturnBadRequest() {
        Long id = 5L;
        when(seasonMapper.toEntity(any(online.rabko.model.Season.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/seasons/{id}", id.toString())
            .then()
            .statusCode(400);
    }

    @Test
    void seasonsIdPut_shouldReturnConflict() {
        Long id = 6L;
        when(seasonMapper.toEntity(any(online.rabko.model.Season.class)))
            .thenReturn(Season.builder().build());
        when(basketballSeasonService.update(eq(id), any(Season.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/seasons/{id}", id.toString())
            .then()
            .statusCode(409);
    }
}
