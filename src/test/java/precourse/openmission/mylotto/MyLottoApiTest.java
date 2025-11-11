package precourse.openmission.mylotto;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import precourse.openmission.ApiTest;
import precourse.openmission.domain.NumberGenerator;
import precourse.openmission.purchase.Purchase;
import precourse.openmission.purchase.PurchaseRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MyLottoApiTest extends ApiTest {
    @Autowired
    MyLottoRepository myLottoRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NumberGenerator numberGenerator() {
            return Mockito.mock(NumberGenerator.class);
        }
    }

    @Autowired
    NumberGenerator numberGenerator;

    Purchase savedPurchase;
    Long purchaseId;

    @BeforeEach
    void setUp() {
        //Given
        myLottoRepository.deleteAll();
        purchaseRepository.deleteAll();

        Purchase purchase = new Purchase(2000, 2);
        savedPurchase = purchaseRepository.save(purchase);
        purchaseId = savedPurchase.getId();
        Mockito.when(numberGenerator.generateNumbers())
                .thenReturn(List.of(1, 2, 3, 4, 5, 6))
                .thenReturn(List.of(7, 8, 9, 10, 11, 12));
    }

    @DisplayName("발행 API 정상 작동 확인")
    @Test
    void issue() {
        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .post("/issue/mylotto/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getList("$")).hasSize(2),
                () -> assertThat(jsonPath.getString("[0].numbers")).isEqualTo("1,2,3,4,5,6"),
                () -> assertThat(jsonPath.getString("[1].numbers")).isEqualTo("7,8,9,10,11,12")
        );
    }

    @DisplayName("조회 API 정상 작동 확인")
    @Test
    void viewMyLotto() {
        //Given
        RestAssured.given()
                .when()
                .post("/issue/mylotto/" + purchaseId)
                .then()
                .extract();

        //When
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/mylotto/" + purchaseId)
                .then()
                .extract();

        //Then
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.header("Content-Type")).isEqualTo("application/json"),
                () -> assertThat(jsonPath.getList("$")).hasSize(2),
                () -> assertThat(jsonPath.getString("[0].numbers")).isEqualTo("1,2,3,4,5,6"),
                () -> assertThat(jsonPath.getString("[1].numbers")).isEqualTo("7,8,9,10,11,12")
        );
    }

    @DisplayName("유효하지 않은 id 입력 시, 에러메시지 반환")
    @Test
    void invalidPurchaseId() {
        //When
        Response errorResponseToPost = RestAssured.given()
                .post("/issue/mylotto/123")
                .then()
                .extract()
                .response();
        //When
        Response errorResponseToGet = RestAssured.given()
                .get("/mylotto/123")
                .then()
                .extract()
                .response();
        //Then
        assertAll(
                () -> assertThat(errorResponseToPost.statusCode()).isEqualTo(400),
                () -> assertThat(errorResponseToPost.body().asString()).isEqualTo("[ERROR] 해당 구매 내역을 찾을 수 없습니다."),
                () -> assertThat(errorResponseToGet.statusCode()).isEqualTo(400),
                () -> assertThat(errorResponseToGet.body().asString()).isEqualTo("[ERROR] 해당 구매 내역을 찾을 수 없습니다.")
        );
    }
}
