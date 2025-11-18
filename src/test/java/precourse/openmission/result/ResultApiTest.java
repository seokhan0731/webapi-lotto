package precourse.openmission.result;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import precourse.openmission.ApiTest;
import precourse.openmission.bonusnumber.BonusNumber;
import precourse.openmission.bonusnumber.BonusNumberRepository;
import precourse.openmission.bonusnumber.BonusNumberService;
import precourse.openmission.mylotto.MyLotto;
import precourse.openmission.mylotto.MyLottoRepository;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ResultApiTest extends ApiTest {
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    WinningRepository winningRepository;
    @Autowired
    BonusNumberRepository bonusRepository;

    @Autowired

    BonusNumberService bonusNumberService;
    @Autowired
    MyLottoRepository myLottoRepository;

    Long purchaseId;
    Purchase savedPurchase;

    @BeforeEach
    void setUp() {
        myLottoRepository.deleteAll();
        winningRepository.deleteAll();
        bonusRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(8000, 8);

        WinningLotto winningLotto = new WinningLotto("1,2,3,4,5,6", purchase);
        WinningLotto savedLotto = winningRepository.save(winningLotto);


        savedPurchase = savedLotto.getPurchase();
        purchaseId = savedPurchase.getId();

        bonusNumberService.saveBonus(7, purchaseId);

        MyLotto lotto1 = new MyLotto("8,21,23,41,42,45", savedPurchase);
        MyLotto lotto2 = new MyLotto("3,5,11,16,32,38", savedPurchase);
        MyLotto lotto3 = new MyLotto("7,11,16,35,36,44", savedPurchase);
        MyLotto lotto4 = new MyLotto("1,8,11,31,41,42", savedPurchase);
        MyLotto lotto5 = new MyLotto("13,14,16,38,42,45", savedPurchase);
        MyLotto lotto6 = new MyLotto("7,11,30,40,42,43", savedPurchase);
        MyLotto lotto7 = new MyLotto("2,13,22,32,38,45", savedPurchase);
        MyLotto lotto8 = new MyLotto("1,3,5,14,22,45", savedPurchase);

        myLottoRepository.saveAll(List.of(lotto1, lotto2, lotto3, lotto4, lotto5, lotto6, lotto7, lotto8));

    }

    @DisplayName("결과(수익률. 순위) 조회 API 정상 작동 확인")
    @Test
    void getProfitRate() {
        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/result/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getLong("purchaseId")).isEqualTo(1),
                () -> assertThat(jsonPath.getDouble("profitRate")).isEqualTo(62.5),
                () -> assertThat(jsonPath.getMap("rankCounts")).containsExactly(
                        entry("First", 0),
                        entry("Second", 0),
                        entry("Third", 0),
                        entry("Fourth", 0),
                        entry("Fifth", 1))
        );
    }

    @DisplayName("유효하지 않은 id로 결과 조회")
    @Test
    void getByFakeId() {
        //Given
        long fakeId = 123L;

        //When
        Response errorResponseToPost = RestAssured.given()
                .get("/result/" + fakeId)
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(400),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 해당 구매 내역을 찾을 수 없습니다.")
        );
    }

    @DisplayName("로또가 발행되지 않은 경우, 예외 처리")
    @Test
    void getResultExceptMyLotto() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        WinningLotto winningLotto = new WinningLotto("1,2,3,4,5,6", purchase);
        winningRepository.save(winningLotto);
        purchaseId = winningLotto.getId();
        bonusNumberService.saveBonus(7, purchaseId);

        //When
        Response errorResponseToPost = RestAssured.given()
                .get("/result/" + purchaseId)
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(409),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 로또 발행이 되어 있지 않습니다.")
        );
    }

    @DisplayName("당첨 로또가 발행되지 않은 경우, 예외 처리")
    @Test
    void getResultExceptWinningLotto() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        purchaseRepository.save(purchase);
        MyLotto lotto1 = new MyLotto("8,21,23,41,42,45", purchase);
        MyLotto lotto2 = new MyLotto("3,5,11,16,32,38", purchase);
        myLottoRepository.saveAll(List.of(lotto1, lotto2));


        //When
        Response errorResponseToPost = RestAssured.given()
                .get("/result/" + lotto1.getPurchase().getId())
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(409),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 당첨 로또 발행이 되어 있지 않습니다.")
        );
    }

    @DisplayName("보너스 번호가 없는 경우, 예외 처리")
    @Test
    void getResultExceptBonus() {
        //Given
        Purchase purchase = new Purchase(2000, 2);
        MyLotto lotto1 = new MyLotto("8,21,23,41,42,45", purchase);
        MyLotto lotto2 = new MyLotto("3,5,11,16,32,38", purchase);
        WinningLotto winningLotto = new WinningLotto("1,2,3,4,5,6", purchase);
        winningRepository.save(winningLotto);
        myLottoRepository.saveAll(List.of(lotto1, lotto2));
        purchaseId = winningLotto.getId();

        //When
        Response errorResponseToPost = RestAssured.given()
                .get("/result/" + purchaseId)
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(409),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 보너스 번호가 입력되어 있지 않습니다.")
        );
    }

}
