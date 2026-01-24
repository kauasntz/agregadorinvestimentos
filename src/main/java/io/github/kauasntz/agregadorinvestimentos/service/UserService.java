package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.controller.CreateUserdDto;
import io.github.kauasntz.agregadorinvestimentos.entity.User;
import io.github.kauasntz.agregadorinvestimentos.repository.UserReposiory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    private UserReposiory userReposiory;

    public UserService(UserReposiory userReposiory) {
        this.userReposiory = userReposiory;
    }

    public UUID createUser(CreateUserdDto createUserdDto) {
        var entity = new User();
        entity.setUsername(createUserdDto.username());
        entity.setEmail(createUserdDto.email());
        entity.setPassword(createUserdDto.password());
        // userId gerado por @GeneratedValue
        // timestamps gerados por @CreationTimestamp e @UpdateTimestamp

        var userSaved = userReposiory.save(entity);
        return userSaved.getUserId();
    }

    public Optional <User> getUserById(String userId){
        return   userReposiory.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return userReposiory.findAll();
    }

    public void deleteById(String userId){
        var id = UUID.fromString(userId);

        var userExists = userReposiory.existsById(id);

        if (userExists) {
            userReposiory.deleteById(id);
        }
    }
}
