package hu.projects.expense_tracker.features.transactions.controllers;

import hu.projects.expense_tracker.common.pagination.PagedResult;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@RequestBody @Valid CreateTransactionDto dto, Authentication authentication) {
        var transactionDto = transactionService.createTransaction(dto, authentication.getName());
        return ResponseEntity.created(URI.create("/api/transactions/" + transactionDto.id())).body(transactionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        transactionService.deleteTransactionById(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable Long id, Authentication authentication) {
        return transactionService.getTransactionById(id, authentication.getName());
    }

    @GetMapping
    public PagedResult<TransactionDto> getTransactions(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        return transactionService.getTransactionsPaged(authentication.getName(), pageable);
    }
}
