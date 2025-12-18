package hu.projects.expense_tracker.features.transactions.controllers;

import hu.projects.expense_tracker.services.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import hu.projects.expense_tracker.features.transactions.dtos.CreateTransactionDto;
import hu.projects.expense_tracker.features.transactions.dtos.TransactionDto;
import hu.projects.expense_tracker.features.transactions.services.TransactionService;
import hu.projects.expense_tracker.common.pagination.PaginationAttributes;

@RestController
@RequestMapping(value = "/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@RequestBody CreateTransactionDto dto, Authentication authentication) {
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
    public ResponseEntity<Page<TransactionDto>> getTransactions(
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            Authentication authentication
    ) {
        var pgAttributes = new PaginationAttributes(size, page);
        var result = transactionService.getTransactionsPaged(authentication.getName(), pgAttributes);
        var headers = HttpService.GeneratePaginationHeaders(result);
        return ResponseEntity.ok().headers(headers).body(result);
    }
}
