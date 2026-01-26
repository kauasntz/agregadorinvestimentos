package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.controller.dto.CreateUserdDto;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.UpdateUserDto;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

            doReturn(Optional.of(user))
                    .when(userReposiory)
                    .findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by ID with success when optional is empty")
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

    @Nested
    class listUsers {

        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {

            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "123",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(userList)
                    .when(userReposiory)
                    .findAll();

            //  Act

            var output = userService.listUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Should delete user with success when user exists")
        void shouldDeleteUserWithSuccessWhenUserExists() {
            // Arrange
            doReturn(true)
                    .when(userReposiory)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userReposiory)
                    .deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userReposiory, times(1)).existsById(idList.get(0));
            verify(userReposiory, times(1)).deleteById(idList.get(1));
        }


        @Test
        @DisplayName("Should not delete user with success when user not exists")
        void shouldNotDeleteUserWithSuccessWhenUserNotExists() {
            // Arrange
            doReturn(false)
                    .when(userReposiory)
                    .existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userReposiory, times(1))
                    .existsById(uuidArgumentCaptor.getValue());
            verify(userReposiory, times(0))
                    .deleteById(any());
        }
    }

    @Nested
    class updateUserById {

        @Test
        @DisplayName("Should update user by ID when user exists and username and password are filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordAreFilled() {
            // Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user))
                    .when(userReposiory)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user)
                    .when(userReposiory )
                    .save(userArgumentCaptor.capture());

            // Act
            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            // Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userReposiory, times(1))
                    .findById(uuidArgumentCaptor.getValue());
            verify(userReposiory, times(1))
                    .save(user);
        }

        @Test
        @DisplayName("Should not update user by ID when user not exists")
        void shouldNotUpdateUserByIdWhenUserNotExists() {
            // Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );
            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userReposiory)
                    .findById(uuidArgumentCaptor.capture());

            // Act
            userService.updateUserById(userId.toString(), updateUserDto);

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userReposiory, times(1))
                    .findById(uuidArgumentCaptor.getValue());
            verify(userReposiory, times(0))
                    .save(any());
        }
    }
}