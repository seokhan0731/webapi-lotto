package precourse.openmission.domain;

/**
 * 로또 당첨 순위당 맞춘 개수와 해당 상금을 View에 전달하기 위한 DTO입니다.
 *
 * @param matchCount 맞춘 숫자 개수
 * @param prize      상금
 */
public record CategoryDTO(int matchCount, int prize) {
}
