package io.github.kauasntz.agregadorinvestimentos.controller;

import io.github.kauasntz.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import io.github.kauasntz.agregadorinvestimentos.controller.dto.CreateAccountDto;
import io.github.kauasntz.agregadorinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                              @RequestBody AssociateAccountStockDto dto) {
        accountService.associateStock(accountId, dto);
        return ResponseEntity.ok().build();
    }
}
