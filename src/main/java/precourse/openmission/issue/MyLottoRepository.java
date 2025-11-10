package precourse.openmission.issue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyLottoRepository extends JpaRepository<MyLotto, Long> {
    List<MyLotto> findByPurchaseId(Long purchaseId);
}
