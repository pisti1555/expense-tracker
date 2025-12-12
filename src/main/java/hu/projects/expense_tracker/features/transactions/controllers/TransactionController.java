package hu.projects.expense_tracker.features.transactions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.services.TransactionService;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;
import java.util.List;

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
    public List<TransactionDto> getTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        var pgAttributes = new PaginationAttributes(size, page);
        return transactionService.getTransactionsPaged(userId, pgAttributes);
    }
}
