package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.query.GetVueloByIdQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;

public interface GetVueloByIdUseCase {
    VueloResponseDto getById(GetVueloByIdQuery query);
}
