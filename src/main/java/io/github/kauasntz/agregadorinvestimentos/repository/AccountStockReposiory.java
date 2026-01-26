package io.github.kauasntz.agregadorinvestimentos.repository;

import io.github.kauasntz.agregadorinvestimentos.entity.AccountStock;
import io.github.kauasntz.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockReposiory extends JpaRepository<AccountStock, AccountStockId> {

}
