package precourse.openmission.purchase;

/**
 * 구매 금액 관련 로직을 수행하는 클래스입니다.
 * 구매 금액의 유효성 검사와 금액만큼의 로또 개수 반환을 수행합니다.
 */
public class Amount {
    private static final int AMOUNT_PER_LOTTO = 1000;
    private int amount;

    /**
     * 구매 금액을 담당하는 Amount 객체를 생성합니다.
     *
     * @param amount 입력된 구매 금액
     * @throws IllegalArgumentException 구입 금액이 양수가 아닌 경우, 1000원 단위로 떨어지지 않는 경우 발생합니다.
     */
    public Amount(int amount) {
        validate(amount);
        this.amount = amount;
    }

    private void validate(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 양수여야합니다.");
        }
        if (amount % AMOUNT_PER_LOTTO != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원으로 나누어 떨어져야합니다.");
        }
    }

    public int getAmount() {
        return amount;
    }

    /**
     * 구매 금액만큼의 발행할 로또 개수를 반환합니다.
     *
     * @return 발행할 로또 개수
     */
    public int buy() {
        return amount / AMOUNT_PER_LOTTO;
    }

}
