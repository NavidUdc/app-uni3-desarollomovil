package com.jcaa.appcrudusers.application.port.in;

import com.jcaa.appcrudusers.application.dto.query.ListVuelosQuery;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import java.util.List;

public interface ListVuelosUseCase {
    List<VueloResponseDto> list(ListVuelosQuery query);
}
