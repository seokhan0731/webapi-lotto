package precourse.openmission.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyLottoController {
    private final MyLottoService myLottoService;

    @PostMapping(value = "/issue/{id}")
    public List<MyLottoResponseDTO> issue(@PathVariable Long id) {
        List<MyLotto> lottos = myLottoService.saveLottos(id);

        List<MyLottoResponseDTO> responseDTOs = lottos.stream()
                .map(lotto -> new MyLottoResponseDTO(lotto))
                .toList();

        return responseDTOs;
    }
}
