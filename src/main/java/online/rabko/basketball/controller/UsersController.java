package online.rabko.basketball.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.rabko.api.UsersApi;
import online.rabko.basketball.controller.mapper.UserMapper;
import online.rabko.basketball.entity.User;
import online.rabko.basketball.service.basketball.BasketballAppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for {@code /users} endpoints. Implements {@link UsersApi}.
 */
@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final BasketballAppUserService basketballAppUserService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<List<online.rabko.model.User>> usersGet() {
        return ResponseEntity.ok(
            basketballAppUserService.findAll().stream()
                .map(userMapper::toDto)
                .toList()
        );
    }

    @Override
    public ResponseEntity<online.rabko.model.User> usersIdGet(Long id) {
        return ResponseEntity.ok(userMapper.toDto(basketballAppUserService.findById(id)));
    }

    @Override
    public ResponseEntity<online.rabko.model.User> usersIdPut(Long id,
        online.rabko.model.User dto) {
        User toUpdate = userMapper.toEntity(dto);
        toUpdate.setId(id);
        User updated = basketballAppUserService.update(id, toUpdate);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<Void> usersIdDelete(Long id) {
        basketballAppUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
