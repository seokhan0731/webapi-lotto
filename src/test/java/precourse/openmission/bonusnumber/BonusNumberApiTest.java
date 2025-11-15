package precourse.openmission.bonusnumber;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import precourse.openmission.ApiTest;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;
import precourse.openmission.winninglotto.WinningLotto;
import precourse.openmission.winninglotto.WinningRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BonusNumberApiTest extends ApiTest {
    @Autowired
    BonusNumberRepository bonusNumberRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    WinningRepository winningRepository;

    Long purchaseId;
    Purchase savedPurchase;
    @Autowired
    private BonusNumberService bonusNumberService;

    @BeforeEach
    void setUp() {
        bonusNumberRepository.deleteAll();
        winningRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(2000, 2);
        WinningLotto newLotto = new WinningLotto("1,2,3,4,5,6", purchase);
        WinningLotto savedLotto = winningRepository.save(newLotto);

        savedPurchase = purchaseRepository.save(purchase);
        purchaseId = savedPurchase.getId();
    }

    @DisplayName("보너스 번호 저장 Api 정상 작동 확인")
    @Test
    void setBonusNumber() {
        //Given
        BonusRequestDTO requestDTO = new BonusRequestDTO();
        requestDTO.setNumber(7);

        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .when()
                .post("/issue/bonus/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getInt("number")).isEqualTo(7),
                () -> assertThat(jsonPath.getLong("purchaseId")).isEqualTo(purchaseId)
        );
    }

    @DisplayName("보너스 번호 조회 api 정상 작동 확인")
    @Test
    void getBonus() {
        //Given
        bonusNumberService.saveBonus(7, purchaseId);

        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/bonus/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getInt("number")).isEqualTo(7),
                () -> assertThat(jsonPath.getLong("purchaseId")).isEqualTo(purchaseId)
        );
    }

    @DisplayName("유효하지 않은 id로 보너스 번호 저장")
    @Test
    void saveByFakeId() {
        //Given
        long fakeId = 123L;
        BonusRequestDTO requestDTO = new BonusRequestDTO();
        requestDTO.setNumber(7);

        //When
        Response errorResponseToPost = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .post("/issue/bonus/" + fakeId)
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

    @DisplayName("유효하지 않은 id로 보너스 번호 조회")
    @Test
    void getByFakeId() {
        //Given
        long fakeId = 123L;
        BonusRequestDTO requestDTO = new BonusRequestDTO();
        requestDTO.setNumber(7);

        //When
        Response errorResponseToPost = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .get("/bonus/" + fakeId)
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

    @DisplayName("유효하지 않은 범위의 보너스 번호 저장")
    @Test
    void saveInvalidRange() {
        //Given
        BonusRequestDTO requestDTO = new BonusRequestDTO();
        requestDTO.setNumber(47);

        //When
        Response errorResponseToPost = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .post("/issue/bonus/" + purchaseId)
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(400),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 보너스 번호는 1~45 사이의 숫자만 가집니다.")
        );
    }

    @DisplayName("당첨 번호와 중복된 보너스 번호 저장")
    @Test
    void saveDuplicateNumber() {
        //Given
        BonusRequestDTO requestDTO = new BonusRequestDTO();
        requestDTO.setNumber(3);

        //When
        Response errorResponseToPost = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .post("/issue/bonus/" + purchaseId)
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(400),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 보너스 번호는 로또 번호와 중복되면 안됩니다.")
        );
    }

    @DisplayName("당첨 번호가 없는 상태에서의 보너스 번호 저장")
    @Test
    void saveBonusExceptWinning() {
        //Given
        Purchase newPurchse = new Purchase(2000, 2);
        long newPurchaseId = purchaseRepository.save(newPurchse).getId();
        BonusRequestDTO requestDTO = new BonusRequestDTO();
        requestDTO.setNumber(3);

        //When
        Response errorResponseToPost = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .post("/issue/bonus/" + newPurchaseId)
                .then()
                .extract()
                .response();

        //Then
        assertAll(
                () -> Assertions.assertThat(errorResponseToPost.statusCode()).isEqualTo(409),
                () -> Assertions.assertThat(errorResponseToPost.body().asString())
                        .isEqualTo("[ERROR] 당첨 번호 입력이 선행되어야 합니다.")
        );
    }

}