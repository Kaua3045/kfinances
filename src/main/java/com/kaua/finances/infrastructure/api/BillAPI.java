package com.kaua.finances.infrastructure.api;

import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.infrastructure.bill.models.BillListResponse;
import com.kaua.finances.infrastructure.bill.models.CreateBillRequest;
import com.kaua.finances.infrastructure.bill.models.UpdateBillRequest;
import com.kaua.finances.infrastructure.bill.models.UpdatePendingBillRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "bills")
@Tag(name = "Bills")
public interface BillAPI {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new bill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "404", description = "Account id was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createBill(@RequestBody CreateBillRequest input);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get bill by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill find successfully"),
            @ApiResponse(responseCode = "404", description = "Bill was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> getById(@PathVariable String id);

    @GetMapping(value = "{accountId}")
    @Operation(summary = "List all bill by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<BillListResponse> listBillsByAccountId(
            @PathVariable String accountId,
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update bill by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "404", description = "Bill was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateBillRequest input);

    @PatchMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update pending bill by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending bill updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "404", description = "Bill was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updatePendingById(@PathVariable String id, @RequestBody UpdatePendingBillRequest input);

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Delete bill by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bill deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable String id);
}
