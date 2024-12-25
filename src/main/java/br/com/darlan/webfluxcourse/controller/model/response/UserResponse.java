package br.com.darlan.webfluxcourse.controller.model.response;

public record UserResponse(
        String id,
        String name,
        String email,
        String password
) {
}
