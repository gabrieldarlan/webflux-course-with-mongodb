package br.com.darlan.webfluxcourse.controller.impl;

import br.com.darlan.webfluxcourse.entity.User;
import br.com.darlan.webfluxcourse.mapper.UserMapper;
import br.com.darlan.webfluxcourse.model.request.UserRequest;
import br.com.darlan.webfluxcourse.model.response.UserResponse;
import br.com.darlan.webfluxcourse.service.UserService;
import br.com.darlan.webfluxcourse.service.exception.ObjectNotFoundException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    public static final String URI = "/users";
    public static final String ID = "123456";
    public static final String NAME = "name";
    public static final String EMAIL = "name@email.com";
    public static final String PASSWORD = "123";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;


    @Test
    @DisplayName("Test endpoint with success")
    void testSaveWithSuccess() {
        // Arrange
        final UserRequest request = new UserRequest(NAME, EMAIL, PASSWORD);
        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri(URI)
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus()
                .isCreated();

        verify(service).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Test endpoint with bad request")
    void testSaveWithBadRequest() {
        // Arrange
        final UserRequest request = new UserRequest(NAME.concat(" "), EMAIL, PASSWORD);

        // Act
        webTestClient.post().uri(URI)
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                // Assert
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(URI)
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo(NAME)
                .jsonPath("$.errors[0].message").isEqualTo("field cannot contain spaces at the beginning or end");
    }


    @Test
    @DisplayName("Test find by id with success")
    void testFindByIdWithSuccess() {
        UserResponse userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get()
                .uri(URI + "/".concat(ID))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123456")
                .jsonPath("$.name").isEqualTo(userResponse.name())
                .jsonPath("$.email").isEqualTo(userResponse.email())
                .jsonPath("$.password").isEqualTo(userResponse.password());

    }

    @Test
    @DisplayName("Test find by id with unsuccessful response (not found)")
    void testFindByIdWithNotFoundReturn() {

        when(service.findById(anyString())).thenThrow(new ObjectNotFoundException(format("Object not found. Id: %s, Type: %s", ID, User.class.getSimpleName())));

        webTestClient.get()
                .uri(URI + "/".concat(ID))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Not Found");
    }


    @Test
    @DisplayName("Test find all with success")
    void testFindAllWithSuccess() {
        UserResponse userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get()
                .uri(URI)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(userResponse.id())
                .jsonPath("$[0].name").isEqualTo(userResponse.name())
                .jsonPath("$[0].email").isEqualTo(userResponse.email())
                .jsonPath("$[0].password").isEqualTo(userResponse.password());
    }

    @Test
    void update() {

        
    }

    @Test
    void delete() {
    }
}