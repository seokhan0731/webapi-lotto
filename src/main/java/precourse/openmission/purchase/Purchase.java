package precourse.openmission.purchase;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리포지토리 계층에서 관리하는 Entity 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Purchase {
    /**
     * 구매 내역의 고유 실별자 (PK)
     * Auto-increment를 통해 자동 생성합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 입력받은 구매 금액
     * Amount 클래스에서 검증받은 구매 금액에 해당합니다.
     */
    private int money;

    /**
     * 구매한 로또의 수량
     */
    private int quantity;

    public Purchase(int money, int quantity) {
        this.money = money;
        this.quantity = quantity;
    }
}
