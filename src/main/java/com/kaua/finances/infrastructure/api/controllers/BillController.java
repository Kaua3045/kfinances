package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.bill.*;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;
import com.kaua.finances.infrastructure.api.BillAPI;
import com.kaua.finances.infrastructure.bill.models.BillListResponse;
import com.kaua.finances.infrastructure.bill.models.CreateBillRequest;
import com.kaua.finances.infrastructure.bill.models.UpdateBillRequest;
import com.kaua.finances.infrastructure.bill.models.UpdatePendingBillRequest;
import com.kaua.finances.infrastructure.bill.presenters.BillApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class BillController implements BillAPI {

    private final CreateBillUseCase createBillUseCase;
    private final GetBillByIdUseCase getBillByIdUseCase;
    private final ListBillByAccountIdUseCase listBillByAccountIdUseCase;
    private final UpdateBillUseCase updateBillUseCase;
    private final UpdatePendingBillUseCase updatePendingBillUseCase;
    private final DeleteBillByIdUseCase deleteBillByIdUseCase;

    public BillController(
            final CreateBillUseCase createBillUseCase,
            final GetBillByIdUseCase getBillByIdUseCase,
            final ListBillByAccountIdUseCase listBillByAccountIdUseCase,
            final UpdateBillUseCase updateBillUseCase,
            final UpdatePendingBillUseCase updatePendingBillUseCase,
            final DeleteBillByIdUseCase deleteBillByIdUseCase
    ) {
        this.createBillUseCase = Objects.requireNonNull(createBillUseCase);
        this.getBillByIdUseCase = Objects.requireNonNull(getBillByIdUseCase);
        this.listBillByAccountIdUseCase = Objects.requireNonNull(listBillByAccountIdUseCase);
        this.updateBillUseCase = Objects.requireNonNull(updateBillUseCase);
        this.updatePendingBillUseCase = Objects.requireNonNull(updatePendingBillUseCase);
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
    public Pagination<BillListResponse> listBillsByAccountId(
            String accountId,
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return this.listBillByAccountIdUseCase
                .execute(accountId, new SearchQuery(page, perPage, search, sort, direction))
                .map(BillApiPresenter::present);
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
    public ResponseEntity<?> updatePendingById(String id, UpdatePendingBillRequest input) {
        final var aBill = this.updatePendingBillUseCase.execute(id, input.pending());

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
