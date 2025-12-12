package hu.projects.expense_tracker.features.transactions.controllers;

import hu.projects.expense_tracker.services.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.services.TransactionService;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;

@RestController
@RequestMapping(value = "/api/transactions", version = "1.0")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDto>  create(
            @RequestBody CreateTransactionDto dto,
            @RequestParam Long userId
    ) {
        var transactionDto = transactionService.createTransaction(dto, userId);
        return ResponseEntity.created(URI.create("/api/v1/transactions/" + transactionDto.id())).body(transactionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        transactionService.deleteTransactionById(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return transactionService.getTransactionById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionDto>> getTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        var pgAttributes = new PaginationAttributes(size, page);
        var result = transactionService.getTransactionsPaged(userId, pgAttributes);
        var headers = HttpService.GeneratePaginationHeaders(result);
        return ResponseEntity.ok().headers(headers).body(result);
    }
}
