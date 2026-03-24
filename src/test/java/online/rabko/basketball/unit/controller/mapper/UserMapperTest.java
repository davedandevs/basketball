package online.rabko.basketball.unit.controller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import online.rabko.basketball.controller.mapper.UserMapper;
import online.rabko.basketball.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * Unit tests for {@link UserMapper}.
 */
class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void toDto_shouldMapAllFields() {
        User entity = User.builder()
            .id(1L)
            .username("alice")
            .password("secret")
            .firstName("Alice")
            .lastName("Wonderland")
            .role(online.rabko.model.Role.ADMIN)
            .build();

        online.rabko.model.User dto = mapper.toDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals("alice", dto.getUsername());
        assertEquals("secret", dto.getPassword());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Wonderland", dto.getLastName());
        assertEquals(online.rabko.model.Role.ADMIN, dto.getRole());
    }

    @Test
    void toDto_shouldHandleNulls() {
        User entity = User.builder()
            .id(2L)
            .username("bob")
            .build();

        online.rabko.model.User dto = mapper.toDto(entity);

        assertEquals(2L, dto.getId());
        assertEquals("bob", dto.getUsername());
        assertNull(dto.getPassword());
        assertNull(dto.getRole());
    }

    @Test
    void toEntity_shouldMapScalars_andIgnoreId() {
        online.rabko.model.User dto = new online.rabko.model.User()
            .id(999L)
            .username("charlie")
            .password("pw")
            .firstName("Charlie")
            .lastName("Brown")
            .role(online.rabko.model.Role.USER);

        User entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("charlie", entity.getUsername());
        assertEquals("pw", entity.getPassword());
        assertEquals("Charlie", entity.getFirstName());
        assertEquals("Brown", entity.getLastName());
        assertEquals(online.rabko.model.Role.USER, entity.getRole());
    }

    @Test
    void toEntity_shouldHandleNulls() {
        online.rabko.model.User dto = new online.rabko.model.User()
            .username("david");

        User entity = mapper.toEntity(dto);

        assertEquals("david", entity.getUsername());
        assertNull(entity.getPassword());
        assertNull(entity.getRole());
    }
}
