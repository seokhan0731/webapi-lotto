package precourse.openmission.issue;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import precourse.openmission.purchase.Purchase;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyLotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numbers;

    @ManyToOne
    private Purchase purchase;

    public MyLotto(String numbers, Purchase purchase) {
        this.numbers = numbers;
        this.purchase = purchase;
    }
}
