package br.com.darlan.webfluxcourse.mapper;

import br.com.darlan.webfluxcourse.entity.User;
import br.com.darlan.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);
}
