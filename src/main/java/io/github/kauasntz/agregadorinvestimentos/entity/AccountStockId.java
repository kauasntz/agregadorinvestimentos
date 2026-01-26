package io.github.kauasntz.agregadorinvestimentos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class AccountStockId {

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "stock_id")
    private UUID stockId;

    public AccountStockId() {
    }

    public AccountStockId(UUID accountid, UUID stockId) {
        this.accountId = accountid;
        this.stockId = stockId;
    }

    public UUID getStockId() {
        return stockId;
    }

    public void setStockId(UUID stockId) {
        this.stockId = stockId;
    }

    public UUID getAccountid() {
        return accountId;
    }

    public void setAccountid(UUID accountid) {
        this.accountId = accountid;
    }
}
