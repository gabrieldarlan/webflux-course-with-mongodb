package br.com.darlan.webfluxcourse.service;

import br.com.darlan.webfluxcourse.entity.User;
import br.com.darlan.webfluxcourse.mapper.UserMapper;
import br.com.darlan.webfluxcourse.model.request.UserRequest;
import br.com.darlan.webfluxcourse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
        // Arrange
        UserRequest request = new UserRequest("gabriel", "gabriel@gmail.com", "123456");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        // Act
        Mono<User> result = service.save(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testFindById() {
        // Arrange
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder()
                .id("123")
                .build()));

        // Act
        Mono<User> result = service.findById("123");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class
                        && Objects.equals(user.getId(), "123"))
                .expectComplete()
                .verify();

        verify(repository, times(1)).findById(anyString());

    }
}