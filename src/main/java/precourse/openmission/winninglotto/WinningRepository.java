package precourse.openmission.winninglotto;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 당첨 번호를 관리하는 DB에 접근하는 repository 계층
 */
public interface WinningRepository extends JpaRepository<WinningLotto, Long> {
    WinningLotto findByPurchaseId(Long purchaseId);
}
