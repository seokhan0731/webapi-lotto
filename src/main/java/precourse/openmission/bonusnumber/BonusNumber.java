package precourse.openmission.bonusnumber;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;

@Entity
@Getter
@NoArgsConstructor
public class BonusNumber {
    @Id
    private Long id;

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
