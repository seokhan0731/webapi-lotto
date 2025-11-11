package precourse.openmission.mylotto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;

/**
 * 발행된 로또를 관리하는 Entity 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyLotto {
    /**
     * 발행 내역의 고유 식별자 (PK)
     * Auto-increment를 통해 자동 생성합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 발행된 로또 번호
     * 문자열 형식으로 숫자간 쉼표로 구분됩니다.
     */
    private String numbers;

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
