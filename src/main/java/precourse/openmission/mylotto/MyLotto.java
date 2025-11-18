package precourse.openmission.mylotto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.singlelottosuperentity.SingleLotto;

/**
 * 발행된 로또를 관리하는 Entity 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyLotto extends SingleLotto {
    /**
     * 발행 내역의 고유 식별자 (PK)
     * Auto-increment를 통해 자동 생성합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * n:1 외래키
     * 한 구매 내역에 해당하는 여러 개의 로또를 발행해야 하기 떄문에 존재합니다.
     */
    @ManyToOne
    private Purchase purchase;

    public MyLotto(String numbers, Purchase purchase) {
        this.numbers = numbers;
        this.purchase = purchase;
    }
}
