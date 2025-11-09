package precourse.openmission.purchase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import precourse.openmission.ApiTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PurchaseApiTest extends ApiTest {
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
}
