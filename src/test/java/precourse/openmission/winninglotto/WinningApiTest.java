package precourse.openmission.winninglotto;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import precourse.openmission.ApiTest;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WinningApiTest extends ApiTest {
    @Autowired
    WinningRepository winningRepository;

    @Autowired
    PurchaseRepository purchaseRepository;


    Long purchaseId;
    @Autowired
    private WinningService winningService;

    @BeforeEach
    void setUp() {
        winningRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(2000, 2);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        purchaseId = savedPurchase.getId();
    }

    @DisplayName("당첨 번호 저장 API 정상 작동 확인")
    @Test
    void setWinningLotto() {
        //Given
        String numbersJson = "{\"numbers\":[1,2,3,4,5,6]}";

        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(numbersJson)
                .when()
                .post("/issue/winninglotto/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getString("numbers")).isEqualTo("1,2,3,4,5,6")
        );
    }

    @DisplayName("당첨 번호 조회 API 정상 작동 확인")
    @Test
    void getWinningLotto() {
        //Given
        winningService.saveLotto(List.of(1, 2, 3, 4, 5, 6), purchaseId);

        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/winninglotto/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getString("numbers")).isEqualTo("1,2,3,4,5,6")
        );
    }
}
