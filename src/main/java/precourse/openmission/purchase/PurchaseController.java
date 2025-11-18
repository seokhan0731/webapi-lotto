package precourse.openmission.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

/**
 * 로또 구매 관련 API 컨트롤러 계층입니다.
 */
@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    /**
     * 구매 금액을 받아 구매 금액과 해당 금액만큼의 로또 수량을 저장합니다.
     *
     * @param purchaseRequestDTO 'monoey' 필드가 포함된 구매 요청 JSON
     * @return HTTP StatusCode 200. 생성된 Purchase 객체(구매 금액, 수량)
     * @throws IllegalArgumentException money가 1000원 단위가 아니거나 양수가 아닌 경우, HTTP Status 400 리턴
     */
    @PostMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseResponseDTO savePurchase(@RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return new PurchaseResponseDTO(purchaseService.purchase(purchaseRequestDTO.getMoney()));
    }

    /**
     * 구매 내역 조회 요청을 받아 구매 내역을 반환합니다.
     *
     * @return HTTP StatusCode 200. Purchase 객체(구매 금액, 수량)의 리스트
     */
    @GetMapping(value = "/purchase/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PurchaseResponseDTO> getHistory() {
        return purchaseService.getPurchases()
                .stream()
                .map(PurchaseResponseDTO::new)
                .toList();
    }
}
