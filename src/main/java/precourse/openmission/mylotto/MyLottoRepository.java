package precourse.openmission.mylotto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 발행된 로또를 관리하는 DB를 책임지는 repository 계층
 * Jpa를 통해 접근하기 때문에 JpaRepository를 상속받아 사용합니다.
 */
public interface MyLottoRepository extends JpaRepository<MyLotto, Long> {
    List<MyLotto> findByPurchaseId(Long purchaseId);
}
