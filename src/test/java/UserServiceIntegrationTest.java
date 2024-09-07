import com.example.weather_project.dao.UserDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.User;
import com.example.weather_project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Transactional
class UserServiceIntegrationTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test
    void shouldRegisterNewUser() {
        User newUser = new User("testUser", "password");
        when(userDAO.getByLogin("testUser")).thenReturn(Optional.of(newUser));

        userService.registration(newUser.getLogin(), newUser.getPassword());

        Optional<User> user = userDAO.getByLogin("testUser");
        assertTrue(user.isPresent(), "user should be present in the database after registration");
    }

    @SneakyThrows
    @Test
    void shouldThrowExceptionWhenRegisteringWithDuplicateUsername() {
        User user1 = new User("duplicateUser", "password");
        when(userDAO.getByLogin("duplicateUser")).thenReturn(Optional.of(user1));

        User user2 = new User("duplicateUser", "password");

        assertThrows(OperateDAOException.class, () -> userService.registration(user2.getLogin(), user2.getPassword()));
    }
}