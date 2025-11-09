package precourse.openmission.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public Purchase savePurchase(@RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return purchaseService.purchase(purchaseRequestDTO.getMoney());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @GetMapping(value = "/purchase/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Purchase> getHistory() {
        return purchaseService.getPurchases();
    }
}
