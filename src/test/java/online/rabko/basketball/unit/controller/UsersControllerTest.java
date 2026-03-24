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
import online.rabko.basketball.controller.UsersController;
import online.rabko.basketball.controller.mapper.UserMapper;
import online.rabko.basketball.entity.User;
import online.rabko.basketball.service.basketball.BasketballAppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Unit tests for {@link UsersController}.
 */
@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private BasketballAppUserService basketballAppUserService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(usersController);
    }

    @Test
    void usersGet_shouldReturnList() {
        User u1 = User.builder().id(1L).username("alice").build();
        User u2 = User.builder().id(2L).username("bob").build();
        when(basketballAppUserService.findAll()).thenReturn(List.of(u1, u2));
        when(userMapper.toDto(u1)).thenReturn(new online.rabko.model.User());
        when(userMapper.toDto(u2)).thenReturn(new online.rabko.model.User());

        given()
            .when()
            .get("/users")
            .then()
            .statusCode(200)
            .body("$", hasSize(2));

        verify(basketballAppUserService).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void usersIdGet_shouldReturnUser() {
        Long id = 42L;
        User entity = User.builder().id(id).username("john").build();
        when(basketballAppUserService.findById(id)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(new online.rabko.model.User());

        given()
            .when()
            .get("/users/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(basketballAppUserService).findById(id);
        verify(userMapper).toDto(entity);
    }

    @Test
    void usersIdPut_shouldUpdateUser() {
        Long id = 7L;
        User toUpdate = User.builder().username("new").build();
        User updated = User.builder().id(id).username("new").build();

        when(userMapper.toEntity(any(online.rabko.model.User.class))).thenReturn(toUpdate);
        when(basketballAppUserService.update(eq(id), eq(toUpdate))).thenReturn(updated);
        when(userMapper.toDto(updated)).thenReturn(new online.rabko.model.User());

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/users/{id}", id.toString())
            .then()
            .statusCode(200)
            .body("$", notNullValue());

        verify(userMapper).toEntity(any(online.rabko.model.User.class));
        verify(basketballAppUserService).update(id, toUpdate);
        verify(userMapper).toDto(updated);
    }

    @Test
    void usersIdDelete_shouldDeleteUser() {
        Long id = 9L;
        doNothing().when(basketballAppUserService).delete(id);

        given()
            .when()
            .delete("/users/{id}", id.toString())
            .then()
            .statusCode(204);

        verify(basketballAppUserService).delete(id);
        verifyNoInteractions(userMapper);
    }

    @Test
    void usersIdGet_shouldReturnNotFound() {
        Long id = 404L;
        when(basketballAppUserService.findById(id)).thenThrow(
            new ResponseStatusException(HttpStatus.NOT_FOUND));

        given()
            .when()
            .get("/users/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void usersIdDelete_shouldReturnNotFound() {
        Long id = 404L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(basketballAppUserService)
            .delete(id);

        given()
            .when()
            .delete("/users/{id}", id.toString())
            .then()
            .statusCode(404);
    }

    @Test
    void usersIdPut_shouldReturnBadRequest() {
        Long id = 5L;
        when(userMapper.toEntity(any(online.rabko.model.User.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .put("/users/{id}", id.toString())
            .then()
            .statusCode(400);
    }
}
