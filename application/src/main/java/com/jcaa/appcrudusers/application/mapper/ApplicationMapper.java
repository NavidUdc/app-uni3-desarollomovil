package com.jcaa.appcrudusers.application.mapper;

import com.jcaa.appcrudusers.application.dto.command.CreateUserCommand;
import com.jcaa.appcrudusers.application.dto.command.CreateVueloCommand;
import com.jcaa.appcrudusers.application.dto.command.LoginCommand;
import com.jcaa.appcrudusers.application.dto.command.UpdateUserCommand;
import com.jcaa.appcrudusers.application.dto.command.UpdateVueloCommand;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import com.jcaa.appcrudusers.domain.model.AuthSession;
import com.jcaa.appcrudusers.domain.model.Credentials;
import com.jcaa.appcrudusers.domain.model.User;
import com.jcaa.appcrudusers.domain.model.UserRegistration;
import com.jcaa.appcrudusers.domain.model.UserUpdate;
import com.jcaa.appcrudusers.domain.model.Vuelo;
import com.jcaa.appcrudusers.domain.model.VueloRegistration;
import com.jcaa.appcrudusers.domain.model.VueloUpdate;
import com.jcaa.appcrudusers.domain.vo.Email;
import com.jcaa.appcrudusers.domain.vo.Password;
import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.UserName;
import com.jcaa.appcrudusers.domain.vo.UserRole;
import com.jcaa.appcrudusers.domain.vo.UserStatus;
import com.jcaa.appcrudusers.domain.vo.VueloId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationMapper {

    public static Credentials toDomain(final LoginCommand command) {
        return new Credentials(
                new Email(command.email()),
                new Password(command.password())
        );
    }

    public static UserRegistration toDomain(final CreateUserCommand command) {
        return new UserRegistration(
                new UserId(command.id()),
                new UserName(command.name()),
                new Email(command.email()),
                new Password(command.password()),
                new UserRole(command.role())
        );
    }

    public static UserUpdate toDomain(final UpdateUserCommand command) {
        Optional<Password> passwordOpt = command.passwordOpt()
                .filter(p -> !p.isEmpty())
                .map(Password::new);
        return new UserUpdate(
                new UserName(command.name()),
                new Email(command.email()),
                passwordOpt,
                new UserRole(command.role()),
                new UserStatus(command.status())
        );
    }

    public static UserResponseDto toResponse(final User user) {
        return new UserResponseDto(
                user.getId().getValue(),
                user.getName().getValue(),
                user.getEmail().getValue(),
                user.getRole().getValue(),
                user.getStatus().getValue()
        );
    }

    public static List<UserResponseDto> toResponse(final List<User> users) {
        return users.stream()
                .map(ApplicationMapper::toResponse)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public static AuthSessionResponseDto toResponse(final AuthSession session) {
        return new AuthSessionResponseDto(
                session.getAccessToken().getValue(),
                session.getTokenType(),
                session.getExpiresInSeconds(),
                session.getReceivedAt()
        );
    }

    public static VueloRegistration toDomain(final CreateVueloCommand command) {
        final Optional<VueloId> idOpt = (command.id() != null && !command.id().trim().isEmpty())
                ? Optional.of(new VueloId(command.id().trim()))
                : Optional.empty();
        return new VueloRegistration(
                idOpt,
                command.fechaCompra(),
                command.fechaSalida(),
                command.fechaLlegada(),
                command.agenciaViajes(),
                command.aerolinea(),
                command.numero(),
                command.estado(),
                command.valor(),
                command.cliente(),
                command.puesto(),
                command.avion(),
                command.aereopuertoSalida(),
                command.aereopuertoLlegada(),
                command.piloto(),
                new UserId(command.usuarioId())
        );
    }

    public static VueloUpdate toDomain(final UpdateVueloCommand command) {
        return new VueloUpdate(
                command.fechaCompra(),
                command.fechaSalida(),
                command.fechaLlegada(),
                command.agenciaViajes(),
                command.aerolinea(),
                command.numero(),
                command.estado(),
                command.valor(),
                command.cliente(),
                command.puesto(),
                command.avion(),
                command.aereopuertoSalida(),
                command.aereopuertoLlegada(),
                command.piloto(),
                new UserId(command.usuarioId())
        );
    }

    public static VueloResponseDto toResponse(final Vuelo vuelo) {
        return new VueloResponseDto(
                vuelo.getId().getValue(),
                vuelo.getFechaCompra(),
                vuelo.getFechaSalida(),
                vuelo.getFechaLlegada(),
                vuelo.getAgenciaViajes(),
                vuelo.getAerolinea(),
                vuelo.getNumero(),
                vuelo.getEstado(),
                vuelo.getValor(),
                vuelo.getCliente(),
                vuelo.getPuesto(),
                vuelo.getAvion(),
                vuelo.getAereopuertoSalida(),
                vuelo.getAereopuertoLlegada(),
                vuelo.getPiloto(),
                vuelo.getUsuarioId().getValue()
        );
    }

    public static List<VueloResponseDto> toVueloResponseList(final List<Vuelo> vuelos) {
        return vuelos.stream()
                .map(ApplicationMapper::toResponse)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}
