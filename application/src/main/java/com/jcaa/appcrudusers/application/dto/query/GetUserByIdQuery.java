package com.jcaa.appcrudusers.application.dto.query;

import java.util.Objects;

public record GetUserByIdQuery(String id) {
    public GetUserByIdQuery {
        Objects.requireNonNull(id, "id");
    }
}
