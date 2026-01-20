package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.controller.CreateUserdDto;
import io.github.kauasntz.agregadorinvestimentos.entity.User;
import io.github.kauasntz.agregadorinvestimentos.repository.UserReposiory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private UserReposiory userReposiory;

    public UserService(UserReposiory userReposiory) {
        this.userReposiory = userReposiory;
    }

        public UUID createUser(CreateUserdDto createUserdDto) {

        //DTO -> Entity
        var entity = new User(
                UUID.randomUUID(),
                createUserdDto.username(),
                createUserdDto.email(),
                createUserdDto.password(),
                Instant.now(),
                null);

        var userSaved = userReposiory.save(entity);
        return userSaved.getUserId();
    }
}
