package precourse.openmission.purchase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import precourse.openmission.ApiTest;
import precourse.openmission.bonusnumber.BonusNumberRepository;
import precourse.openmission.mylotto.MyLottoRepository;
import precourse.openmission.winninglotto.WinningRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PurchaseApiTest extends ApiTest {
    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    MyLottoRepository myLottoRepository;
    @Autowired
    WinningRepository winningRepository;
    @Autowired
    BonusNumberRepository bonusNumberRepository;

    @BeforeEach
    void setUp() {
        myLottoRepository.deleteAll();
        winningRepository.deleteAll();
        bonusNumberRepository.deleteAll();
        purchaseRepository.deleteAll();
    }


    @DisplayName("구매 API 정상 작동 확인")
    @Test
    void purchase() {
        //Given
        String purchaseJson = "{\"money\": 2000}";

        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(purchaseJson)
                .when()
                .post("/purchase")
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getInt("money")).isEqualTo(2000),
                () -> assertThat(jsonPath.getInt("quantity")).isEqualTo(2)
        );
    }

    @DisplayName("구입 금액 1000원으로 나누어 떨어지지 않는 경우 예외처리")
    @Test
    void amountDivideException() {
        //Given
        String invalidJson = "{\"money\": 1500}";

        //When
        Response errorResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidJson)
                .when()
                .post("/purchase")
                .then()
                .extract()
                .response();

        //Then
        assertThat(errorResponse.statusCode()).isEqualTo(400);
        assertThat(errorResponse.body().asString()).isEqualTo("[ERROR] 구입 금액은 1,000원으로 나누어 떨어져야합니다.");
    }

    @DisplayName("구입 금액 양수가 아닌 경우 예외처리")
    @Test
    void notPositiveMoneyException() {
        //Given
        String invalidJson = "{\"money\": -1500}";

        //When
        Response errorResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidJson)
                .when()
                .post("/purchase")
                .then()
                .extract()
                .response();

        //Then
        assertThat(errorResponse.statusCode()).isEqualTo(400);
        assertThat(errorResponse.body().asString()).isEqualTo("[ERROR] 구입 금액은 양수여야합니다.");
    }

    @DisplayName("구매내역 조회 API 정상 작동 확인")
    @Test
    void viewAllPurchaseHistory() {
        //Given
        purchaseRepository.save(new Purchase(2000, 2));
        purchaseRepository.save(new Purchase(3000, 3));
        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/purchase/history")
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getList("$")).hasSize(2),
                () -> assertThat(jsonPath.getInt("[0].money")).isEqualTo(2000),
                () -> assertThat(jsonPath.getInt("[0].quantity")).isEqualTo(2),
                () -> assertThat(jsonPath.getInt("[1].money")).isEqualTo(3000),
                () -> assertThat(jsonPath.getInt("[1].quantity")).isEqualTo(3)
        );
    }


}
