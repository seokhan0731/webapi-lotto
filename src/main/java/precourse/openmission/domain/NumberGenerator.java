package precourse.openmission.domain;

import java.util.List;

/**
 * 숫자 생성 클래스의 인터페이스입니다.
 * 테스트 코드에서의 가짜 숫자 생서기를 위해 존재합니다.
 */
public interface NumberGenerator {
    List<Integer> generateNumbers();
}
