package io.github.kauasntz.agregadorinvestimentos.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_stocks")
public class Stock {

    @Id
    @Column(name = "stock_id")
    private UUID stockId;

    @Column(name = "description")
    private String description;

    public Stock() {
    }

    public Stock(UUID stockId, String description) {
        this.stockId = stockId;
        this.description = description;
    }

    public UUID getStockId() {
        return stockId;
    }

    public void setStockId(UUID stockId) {
        this.stockId = stockId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
