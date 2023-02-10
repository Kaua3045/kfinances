package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.bill.CreateBillUseCase;
import com.kaua.finances.infrastructure.api.BillAPI;
import com.kaua.finances.infrastructure.bill.models.CreateBillRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class BillController implements BillAPI {

    private final CreateBillUseCase createBillUseCase;

    public BillController(final CreateBillUseCase createBillUseCase) {
        this.createBillUseCase = Objects.requireNonNull(createBillUseCase);
    }

    @Override
    public ResponseEntity<?> createBill(CreateBillRequest input) {
        final var aBill = this.createBillUseCase.execute(
                input.accountId(),
                input.title(),
                input.description(),
                input.pending()
        );

        if (aBill.isLeft()) {
            throw aBill.getLeft();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(aBill.getRight());
    }
}
