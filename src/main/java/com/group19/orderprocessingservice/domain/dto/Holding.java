package com.group19.orderprocessingservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holding {
    private int quantity;
    private double value;

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
    public void increaseValue(double value) {
        this.value += value;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void decreaseValue(double value) {
        this.value -= value;
    }
}
