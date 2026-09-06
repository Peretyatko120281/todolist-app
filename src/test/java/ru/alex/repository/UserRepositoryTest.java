package ru.alex.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailIgnoreCase_ShouldReturnUser_WhenEmailExists() {
        // given
        User user = new User("Test User", "test@example.com", "password", UserRole.USER);
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByEmailIgnoreCase("test@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getName()).isEqualTo("Test User");
    }

    @Test
    void findByEmailIgnoreCase_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        // when
        Optional<User> found = userRepository.findByEmailIgnoreCase("nonexistent@example.com");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void saveUser_ShouldPersistUser() {
        // given
        User user = new User("New User", "new@example.com", "password", UserRole.USER);

        // when
        User saved = userRepository.save(user);

        // then
        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getName()).isEqualTo("New User");
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
        assertThat(saved.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void existsByEmailIgnoreCase_ShouldReturnTrue_WhenEmailExists() {
        // given
        User user = new User("Test", "exists@example.com", "password", UserRole.USER);
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmailIgnoreCase("exists@example.com");

        // then
        assertThat(exists).isTrue();
    }
}