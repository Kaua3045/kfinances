package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.bill.CreateBillUseCase;
import com.kaua.finances.application.usecases.bill.DeleteBillByIdUseCase;
import com.kaua.finances.application.usecases.bill.GetBillByIdUseCase;
import com.kaua.finances.application.usecases.bill.UpdateBillUseCase;
import com.kaua.finances.infrastructure.api.BillAPI;
import com.kaua.finances.infrastructure.bill.models.CreateBillRequest;
import com.kaua.finances.infrastructure.bill.models.UpdateBillRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class BillController implements BillAPI {

    private final CreateBillUseCase createBillUseCase;
    private final GetBillByIdUseCase getBillByIdUseCase;
    private final UpdateBillUseCase updateBillUseCase;
    private final DeleteBillByIdUseCase deleteBillByIdUseCase;

    public BillController(
            final CreateBillUseCase createBillUseCase,
            final GetBillByIdUseCase getBillByIdUseCase,
            final UpdateBillUseCase updateBillUseCase,
            final DeleteBillByIdUseCase deleteBillByIdUseCase
    ) {
        this.createBillUseCase = Objects.requireNonNull(createBillUseCase);
        this.getBillByIdUseCase = Objects.requireNonNull(getBillByIdUseCase);
        this.updateBillUseCase = Objects.requireNonNull(updateBillUseCase);
        this.deleteBillByIdUseCase = Objects.requireNonNull(deleteBillByIdUseCase);
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

    @Override
    public ResponseEntity<?> updateById(String id, UpdateBillRequest input) {
        final var aBill = this.updateBillUseCase.execute(
                id,
                input.title(),
                input.description(),
                input.pending()
        );

        if (aBill.isLeft()) {
            throw aBill.getLeft();
        }

        return ResponseEntity.ok().body(aBill.getRight());
    }

    @Override
    public void deleteById(String id) {
        this.deleteBillByIdUseCase.execute(id);
    }
}
