package precourse.openmission.bonusnumber;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 보너스 번호 DB에 접근하는 repository 계층입니다.
 */
public interface BonusNumberRepository extends JpaRepository<BonusNumber, Long> {
}
