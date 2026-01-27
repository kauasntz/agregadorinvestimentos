package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.controller.dto.AccountResponseDto;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.CreateAccountDto;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.CreateUserdDto;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.UpdateUserDto;
import io.github.kauasntz.agregadorinvestimentos.entity.Account;
import io.github.kauasntz.agregadorinvestimentos.entity.BillingAddress;
import io.github.kauasntz.agregadorinvestimentos.entity.User;
import io.github.kauasntz.agregadorinvestimentos.repository.AccountReposiory;
import io.github.kauasntz.agregadorinvestimentos.repository.BillingaddressReposiory;
import io.github.kauasntz.agregadorinvestimentos.repository.UserReposiory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    private UserReposiory userReposiory;
    private AccountReposiory accountReposiory;
    private BillingaddressReposiory billingaddressReposiory;

    public UserService(UserReposiory userReposiory,
                       AccountReposiory accountReposiory,
                       BillingaddressReposiory billingaddressReposiory) {
        this.userReposiory = userReposiory;
        this.accountReposiory = accountReposiory;
        this.billingaddressReposiory = billingaddressReposiory;
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

    public void updateUserById(String userId,
                               UpdateUserDto updateUserDto) {

        var id = UUID.fromString(userId);

        var userEntity = userReposiory.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }

            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            userReposiory.save(user);
        }
    }

    public void deleteById(String userId){
        var id = UUID.fromString(userId);

        var userExists = userReposiory.existsById(id);

        if (userExists) {
            userReposiory.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {

        var user = userReposiory.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account();
        account.setUser(user);
        account.setDescription(createAccountDto.description());

        var accountCreated = accountReposiory.save(account);

        var billingAddress = new BillingAddress(
                accountCreated,
                createAccountDto.street(),
                createAccountDto.number()
        );

        billingaddressReposiory.save(billingAddress);
    }

    public List <AccountResponseDto> listAccounts(String userId) {

        var user = userReposiory.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts()
                .stream()
                .map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
