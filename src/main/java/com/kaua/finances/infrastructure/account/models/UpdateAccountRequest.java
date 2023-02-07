package com.kaua.finances.infrastructure.account.models;

import java.util.List;

public record UpdateAccountRequest(
        String name,
        String password,
        List<String> bills
) {
}
