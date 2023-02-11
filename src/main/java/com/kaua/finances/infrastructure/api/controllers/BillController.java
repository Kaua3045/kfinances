package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.bill.CreateBillUseCase;
import com.kaua.finances.application.usecases.bill.GetBillByIdUseCase;
import com.kaua.finances.infrastructure.api.BillAPI;
import com.kaua.finances.infrastructure.bill.models.CreateBillRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class BillController implements BillAPI {

    private final CreateBillUseCase createBillUseCase;
    private final GetBillByIdUseCase getBillByIdUseCase;

    public BillController(
            final CreateBillUseCase createBillUseCase,
            final GetBillByIdUseCase getBillByIdUseCase
    ) {
        this.createBillUseCase = Objects.requireNonNull(createBillUseCase);
        this.getBillByIdUseCase = Objects.requireNonNull(getBillByIdUseCase);
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

    @Override
    public ResponseEntity<?> getById(String id) {
        final var aBill = this.getBillByIdUseCase.execute(id);

        return ResponseEntity.ok().body(aBill);
    }
}
