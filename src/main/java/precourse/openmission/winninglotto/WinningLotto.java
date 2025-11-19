package precourse.openmission.winninglotto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.singlelottosuperentity.SingleLotto;

/**
 * 당첨 번호를 관리하는 Entity 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WinningLotto extends SingleLotto {
    /**
     * 발행 내역의 PK
     * 구매 내역의 id를 참조하여 같은 값을 가집니다.
     */
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Purchase purchase;

    public WinningLotto(String numbers, Purchase purchase) {
        this.numbers = numbers;
        this.purchase = purchase;
    }
}
