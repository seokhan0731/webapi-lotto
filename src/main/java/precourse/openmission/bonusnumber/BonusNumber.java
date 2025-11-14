package precourse.openmission.bonusnumber;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;

/**
 * 보너스 번호의 Entity입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class BonusNumber {
    /**
     * 보너스 번호의 Pk
     * 구매 내역의 id를 참조하여 같은 값을 가집니다.
     */
    @Id
    private Long id;

    /**
     * 유효성 검사를 거친 보너스 번호
     */
    private int number;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Purchase purchase;

    public BonusNumber(int number, Purchase purchase) {
        this.number = number;
        this.purchase = purchase;
    }
}
