package precourse.openmission.bonusnumber;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BonusNumberController {
    private final BonusNumberService bonusNumberService;

    @PostMapping("/issue/bonus/{id}")
    public BonusResponseDTO saveBonus(@PathVariable Long id, @RequestBody BonusRequestDTO bonusRequestDTO) {
        BonusNumber newBonus = bonusNumberService.saveBonus(bonusRequestDTO.getNumber(), id);
        return bonusToDTO(newBonus);
    }

    private BonusResponseDTO bonusToDTO(BonusNumber bonusNumber) {
        return new BonusResponseDTO(bonusNumber);
    }

    @GetMapping("/bonus/{id}")
    public BonusResponseDTO getBonus(@PathVariable Long id) {
        BonusNumber savedBonus = bonusNumberService.getBonus(id);
        return bonusToDTO(savedBonus);
    }

}
