package precourse.openmission.winning;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WinningRepository extends JpaRepository<WinningLotto, Long> {
    WinningLotto findByPurchaseId(Long purchaseId);
}
