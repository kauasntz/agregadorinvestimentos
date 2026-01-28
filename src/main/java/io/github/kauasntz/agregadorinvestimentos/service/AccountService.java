package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import io.github.kauasntz.agregadorinvestimentos.entity.AccountStock;
import io.github.kauasntz.agregadorinvestimentos.entity.AccountStockId;
import io.github.kauasntz.agregadorinvestimentos.repository.AccountReposiory;
import io.github.kauasntz.agregadorinvestimentos.repository.AccountStockReposiory;
import io.github.kauasntz.agregadorinvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AccountService {

    private AccountReposiory accountReposiory;

    private StockRepository stockRepository;

    private AccountStockReposiory accountStockReposiory;

    public AccountService(AccountReposiory accountReposiory,
                          StockRepository stockRepository,
                          AccountStockReposiory accountStockReposiory) {
        this.accountReposiory = accountReposiory;
        this.stockRepository = stockRepository;
        this.accountStockReposiory = accountStockReposiory;
    }

    public void associateStock(String accountId, AssociateAccountStockDto dto) {

        var account = accountReposiory.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(dto.stockId()  )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //  DTO -> ENTITY
        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );

        accountStockReposiory.save(entity);
    }
}
