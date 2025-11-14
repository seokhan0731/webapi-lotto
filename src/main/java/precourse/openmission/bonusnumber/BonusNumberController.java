package precourse.openmission.bonusnumber;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
