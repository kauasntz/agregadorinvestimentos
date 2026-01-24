package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.controller.CreateUserdDto;
import io.github.kauasntz.agregadorinvestimentos.entity.User;
import io.github.kauasntz.agregadorinvestimentos.repository.UserReposiory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserReposiory userReposiory;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateUserWithSuccess() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(user).when(userReposiory).save(userArgumentCaptor.capture());

            var input = new CreateUserdDto(
                    "username",
                    "email@email.com",
                    "123"
            );

            // Act
            var output = userService.createUser(input);

            // Assert
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            // Arrange
            doThrow(new RuntimeException()).when(userReposiory).save(any());
            var input = new CreateUserdDto(
                    "username",
                    "email@email.com",
                    "123"
            );

            // Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserById{

        @Test
        @DisplayName("Should get user by ID with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionIsPresent() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user)).when(userReposiory).findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by ID with success when optional is emmpty")
        void shouldGetUserByIdWithSuccessWhenOptionIsEmpty() {
            // Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(userReposiory)
                    .findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }
}