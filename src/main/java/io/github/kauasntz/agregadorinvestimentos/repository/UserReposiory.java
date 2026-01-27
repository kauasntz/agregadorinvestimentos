package io.github.kauasntz.agregadorinvestimentos.repository;

import io.github.kauasntz.agregadorinvestimentos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserReposiory extends JpaRepository<User, UUID> {
}
