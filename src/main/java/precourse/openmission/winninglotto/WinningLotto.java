package precourse.openmission.winninglotto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WinningLotto {
    @Id
    private Long id;

    private String numbers;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Purchase purchase;


    public WinningLotto(String numbers, Purchase purchase) {
        this.numbers = numbers;
        this.purchase = purchase;
    }
}
