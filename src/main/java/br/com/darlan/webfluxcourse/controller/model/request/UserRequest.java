package br.com.darlan.webfluxcourse.controller.model.request;

public record UserRequest(
        String name,
        String email,
        String password
) {

}
