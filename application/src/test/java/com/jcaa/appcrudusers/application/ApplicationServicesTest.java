package com.jcaa.appcrudusers.application;

import com.jcaa.appcrudusers.application.dto.command.CreateUserCommand;
import com.jcaa.appcrudusers.application.dto.command.DeleteUserCommand;
import com.jcaa.appcrudusers.application.dto.command.LoginCommand;
import com.jcaa.appcrudusers.application.dto.command.UpdateUserCommand;
import com.jcaa.appcrudusers.application.dto.query.GetUserByIdQuery;
import com.jcaa.appcrudusers.application.dto.query.ListUsersQuery;
import com.jcaa.appcrudusers.application.dto.response.AuthSessionResponseDto;
import com.jcaa.appcrudusers.application.dto.response.UserResponseDto;
import com.jcaa.appcrudusers.application.service.*;
import com.jcaa.appcrudusers.domain.model.AuthSession;
import com.jcaa.appcrudusers.domain.model.User;
import com.jcaa.appcrudusers.domain.port.out.*;
import com.jcaa.appcrudusers.domain.vo.AccessToken;
import com.jcaa.appcrudusers.domain.vo.Email;
import com.jcaa.appcrudusers.domain.vo.UserId;
import com.jcaa.appcrudusers.domain.vo.UserName;
import com.jcaa.appcrudusers.domain.vo.UserRole;
import com.jcaa.appcrudusers.domain.vo.UserStatus;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationServicesTest {

    @Test
    public void testLoginService() {
        Instant now = Instant.parse("2026-10-26T12:00:00Z");
        AuthSession expectedSession = new AuthSession(new AccessToken("jwt-token"), "Bearer", 3600, now);

        LoginPort loginPort = credentials -> {
            assertEquals("test@example.com", credentials.getEmail().getValue());
            assertEquals("password123", credentials.getPassword().getValue());
            return expectedSession;
        };

        SaveSessionPort saveSessionPort = session -> assertEquals(expectedSession, session);

        LoginService service = new LoginService(loginPort, saveSessionPort);
        AuthSessionResponseDto response = service.login(new LoginCommand("test@example.com", "password123"));

        assertEquals("jwt-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600, response.expiresIn());
    }

    @Test
    public void testLogoutService() {
        boolean[] cleared = {false};
        ClearSessionPort clearSessionPort = () -> cleared[0] = true;

        LogoutService service = new LogoutService(clearSessionPort);
        service.logout();
        assertTrue(cleared[0]);
    }

    @Test
    public void testGetSessionService() {
        Instant now = Instant.parse("2026-10-26T12:00:00Z");
        AuthSession expectedSession = new AuthSession(new AccessToken("jwt-token"), "Bearer", 3600, now);

        LoadSessionPort loadSessionPort = () -> Optional.of(expectedSession);

        GetSessionService service = new GetSessionService(loadSessionPort);
        Optional<AuthSessionResponseDto> responseOpt = service.getSession();

        assertTrue(responseOpt.isPresent());
        assertEquals("jwt-token", responseOpt.get().accessToken());
    }

    @Test
    public void testCreateUserService() {
        User expectedUser = new User(
                new UserId("user-1"),
                new UserName("John Doe"),
                new Email("john@example.com"),
                new UserRole("ADMIN"),
                new UserStatus("ACTIVE")
        );

        CreateUserPort createUserPort = registration -> {
            assertEquals("user-1", registration.getId().getValue());
            assertEquals("John Doe", registration.getName().getValue());
            return expectedUser;
        };

        CreateUserService service = new CreateUserService(createUserPort);
        UserResponseDto response = service.create(new CreateUserCommand(
                "user-1", "John Doe", "john@example.com", "password123", "ADMIN"
        ));

        assertEquals("user-1", response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        assertEquals("ADMIN", response.role());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    public void testGetUserByIdService() {
        User expectedUser = new User(
                new UserId("user-1"),
                new UserName("John Doe"),
                new Email("john@example.com"),
                new UserRole("ADMIN"),
                new UserStatus("ACTIVE")
        );

        GetUserByIdPort getUserByIdPort = id -> {
            assertEquals("user-1", id.getValue());
            return expectedUser;
        };

        GetUserByIdService service = new GetUserByIdService(getUserByIdPort);
        UserResponseDto response = service.getById(new GetUserByIdQuery("user-1"));

        assertEquals("user-1", response.id());
    }

    @Test
    public void testListUsersService() {
        User user = new User(
                new UserId("user-1"),
                new UserName("John Doe"),
                new Email("john@example.com"),
                new UserRole("ADMIN"),
                new UserStatus("ACTIVE")
        );

        ListUsersPort listUsersPort = () -> Collections.singletonList(user);

        ListUsersService service = new ListUsersService(listUsersPort);
        List<UserResponseDto> responseList = service.list(new ListUsersQuery());

        assertEquals(1, responseList.size());
        assertEquals("user-1", responseList.get(0).id());
    }

    @Test
    public void testUpdateUserService() {
        User expectedUser = new User(
                new UserId("user-1"),
                new UserName("John Updated"),
                new Email("john.updated@example.com"),
                new UserRole("USER"),
                new UserStatus("ACTIVE")
        );

        UpdateUserPort updateUserPort = (id, update) -> {
            assertEquals("user-1", id.getValue());
            assertEquals("John Updated", update.getName().getValue());
            return expectedUser;
        };

        UpdateUserService service = new UpdateUserService(updateUserPort);
        UserResponseDto response = service.update(new UpdateUserCommand(
                "user-1", "John Updated", "john.updated@example.com", null, "USER", "ACTIVE"
        ));

        assertEquals("user-1", response.id());
        assertEquals("John Updated", response.name());
    }

    @Test
    public void testDeleteUserService() {
        String[] deletedId = {null};
        DeleteUserPort deleteUserPort = id -> deletedId[0] = id.getValue();

        DeleteUserService service = new DeleteUserService(deleteUserPort);
        service.delete(new DeleteUserCommand("user-1"));

        assertEquals("user-1", deletedId[0]);
    }
}
