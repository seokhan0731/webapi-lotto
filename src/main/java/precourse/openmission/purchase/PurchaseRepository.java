package precourse.openmission.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import precourse.openmission.issue.MyLotto;

import java.util.List;

/**
 * DB를 관리하는 로직을 갖는 repository 계층
 * Jpa를 통해 접근하기 때문에 JpaRepository를 상속받아 사용합니다.
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
