package precourse.openmission.pojo;

/**
 * 순위당 상금과 해당 순위에 당첨된 횟수를 View에 전달하기 위한 DTO입니다.
 *
 * @param prize  순위당 상금
 * @param number 해당 순위에 당첨된 횟수
 */
public record ResultDTO(int prize, int number) {
}
