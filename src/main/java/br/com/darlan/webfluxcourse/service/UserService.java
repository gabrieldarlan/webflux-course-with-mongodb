package br.com.darlan.webfluxcourse.service;

import br.com.darlan.webfluxcourse.entity.User;
import br.com.darlan.webfluxcourse.mapper.UserMapper;
import br.com.darlan.webfluxcourse.model.request.UserRequest;
import br.com.darlan.webfluxcourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }
}
