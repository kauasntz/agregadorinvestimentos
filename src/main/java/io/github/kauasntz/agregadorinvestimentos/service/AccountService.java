package io.github.kauasntz.agregadorinvestimentos.service;

import io.github.kauasntz.agregadorinvestimentos.client.BrapiClient;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import io.github.kauasntz.agregadorinvestimentos.entity.AccountStock;
import io.github.kauasntz.agregadorinvestimentos.entity.AccountStockId;
import io.github.kauasntz.agregadorinvestimentos.repository.AccountReposiory;
import io.github.kauasntz.agregadorinvestimentos.repository.AccountStockReposiory;
import io.github.kauasntz.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import java.util.UUID;

@Service
public class AccountService {

    @Value("${TOKEN}")
    private String token;

    private AccountReposiory accountReposiory;

    private StockRepository stockRepository;

    private AccountStockReposiory accountStockReposiory;

    private BrapiClient brapiClient;

    public AccountService(AccountReposiory accountReposiory,
                          StockRepository stockRepository,
                          AccountStockReposiory accountStockReposiory, BrapiClient brapiClient) {
        this.accountReposiory = accountReposiory;
        this.stockRepository = stockRepository;
        this.accountStockReposiory = accountStockReposiory;
        this.brapiClient = brapiClient;
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

    public List<AccountStockResponseDto> listStocks(String accountId) {

        var account = accountReposiory.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as -> new AccountStockResponseDto(as.getStock().getStockId(),
                        as.getQuantity(),
                        getTotal(as.getQuantity(), as.getStock().getStockId())
                ))
                .toList();
    }

    private double getTotal(Integer quantity, String stockId) {

        var response = brapiClient.getQuote(token, stockId);

        var price = response.results().getFirst().regularMarketPrice();

        return quantity * price;
    }
}
