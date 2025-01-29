package reqres;

import static org.assertj.core.api.Assertions.assertThat;

import api.ApiResponse;
import api.service.ReqResApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import utils.JsonUtil;

public class UsersTest {

  @Test(description = "Verify Response Structure")
  public void testVerifyResponseStructure() {
    AtomicReference<ApiResponse<JsonElement>> response = new AtomicReference<>();
    Allure.step("Call getUsers api",
        () -> response.set(ReqResApi.getUsers(Map.of("page", 2))));

    Allure.step("Verify content-type in headers");
    String contentType = response.get().headers().get("Content-Type").toString();
    assertThat(contentType).as("Content Type").isEqualTo("application/json; charset=utf-8");

    Allure.step("Verify response body has all fields");
    JsonObject responseBody = response.get().body().getAsJsonObject();
    assertThat(responseBody.has("page")).as("Response fields").isTrue();
    assertThat(responseBody.has("per_page")).as("Response fields").isTrue();
    assertThat(responseBody.has("total")).as("Response fields").isTrue();
    assertThat(responseBody.has("total_pages")).as("Response fields").isTrue();
    assertThat(responseBody.has("data")).as("Response fields").isTrue();
    assertThat(responseBody.has("support")).as("Response fields").isTrue();

    Allure.step("Verify pagination values");
    assertThat(responseBody.get("page").getAsInt()).isEqualTo(2);
    assertThat(responseBody.get("per_page").getAsInt()).isEqualTo(6);
    assertThat(responseBody.get("total").getAsInt()).isEqualTo(12);
    assertThat(responseBody.get("total_pages").getAsInt()).isEqualTo(2);

    Allure.step("Verify data size is 6");
    assertThat(responseBody.getAsJsonArray("data").size()).as("Data size").isEqualTo(6);

    Allure.step("Verify data has all fields");
    for (JsonElement user : responseBody.getAsJsonArray("data")) {
      JsonObject userObj = user.getAsJsonObject();
      assertThat(userObj.has("id")).as("User data fields").isTrue();
      assertThat(userObj.has("email")).as("User data fields").isTrue();
      assertThat(userObj.has("first_name")).as("User data fields").isTrue();
      assertThat(userObj.has("last_name")).as("User data fields").isTrue();
      assertThat(userObj.has("avatar")).as("User data fields").isTrue();
    }

    Allure.step("Verify support data has all fields");
    JsonObject support = responseBody.getAsJsonObject("support");
    assertThat(support.has("url")).as("support data fields").isTrue();
    assertThat(support.has("text")).as("support data fields").isTrue();
  }

  @Test(description = "Verify Invalid Page Number")
  public void testVerifyInvalidPageNumber() {
    AtomicReference<ApiResponse<JsonElement>> response = new AtomicReference<>();

    Allure.step("Call getUsers api with page value higher than total_pages",
        () -> response.set(ReqResApi.getUsers(Map.of("page", 3))));

    Allure.step("Verify response");
    JsonObject responseBody = response.get().body().getAsJsonObject();
    assertThat(responseBody.getAsJsonArray("data").size()).isZero();
    assertThat(responseBody.get("total_pages").getAsInt()).isEqualTo(2);

    Allure.step("Call getUsers api with large page value",
        () -> response.set(ReqResApi.getUsers(Map.of("page", 999))));

    Allure.step("Verify response");
    responseBody = response.get().body().getAsJsonObject();
    assertThat(responseBody.getAsJsonArray("data").size()).isZero();
    assertThat(responseBody.get("total_pages").getAsInt()).isEqualTo(2);

    Allure.step("Call getUsers api with negative value page",
        () -> response.set(ReqResApi.getUsers(Map.of("page", -1))));

    Allure.step("Verify response");
    assertThat(response.get().body().getAsJsonObject().has("error")).as("Error response")
        .isTrue();
  }

  @Test(description = "Verify Correctness Of User Data")
  public void testVerifyCorrectnessOfUserData() {
    JsonObject usersTestData = JsonUtil.readFile("src/test/resources/testdata/UsersTest.json")
        .getAsJsonObject();
    for (int pageCount = 1; pageCount <= 2; pageCount++) {
      AtomicReference<JsonObject> response = new AtomicReference<>();
      int finalPageCount = pageCount;
      Allure.step("Call getUsers api for page %d".formatted(pageCount), () -> response.set(
          ReqResApi.getUsers(Map.of("page", finalPageCount)).body().getAsJsonObject()));

      JsonArray usersData = response.get().getAsJsonArray("data");

      String page = "page%d".formatted(pageCount);
      assertThat(usersData.size()).as("Users data size")
          .isEqualTo(usersTestData.getAsJsonArray(page).size());

      for (int user = 0; user < usersData.size(); user++) {
        JsonObject data = usersData.getAsJsonArray().get(user).getAsJsonObject();
        JsonObject testData = usersTestData.get(page).getAsJsonArray().get(user)
            .getAsJsonObject();

        Allure.step("Verify user %d data".formatted(data.get("id").getAsInt()));
        assertThat(data.get("id")).as("id").isEqualTo(testData.get("id"));
        assertThat(data.get("email")).as("email").isEqualTo(testData.get("email"));
        assertThat(data.get("first_name")).as("first_name").isEqualTo(testData.get("first_name"));
        assertThat(data.get("last_name")).as("last_name").isEqualTo(testData.get("last_name"));
        assertThat(data.get("avatar")).as("avatar").isEqualTo(testData.get("avatar"));
      }
    }
  }

  @Test(description = "Verify Data Consistency For Repeated Requests")
  public void testVerifyDataConsistencyForRepeatedRequests() {
    AtomicReference<JsonObject> response1 = new AtomicReference<>();
    AtomicReference<JsonObject> response2 = new AtomicReference<>();
    Allure.step("First request",
        () -> response1.set(ReqResApi.getUsers(Map.of("page", 2)).body().getAsJsonObject()));
    Allure.step("Second request",
        () -> response2.set(ReqResApi.getUsers(Map.of("page", 2)).body().getAsJsonObject()));

    Allure.step("Verify Data Consistency For Both Requests");
    assertThat(response1.get()).isEqualTo(response2.get());
  }

  @Test(description = "Verify Status For No QueryParameters")
  public void testVerifyResponseForMissingQueryParameters() {
    AtomicReference<JsonObject> response = new AtomicReference<>();
    Allure.step("Call getUsers api with no QueryParameters", () -> {
      response.set(ReqResApi.getUsers(Map.of()).body().getAsJsonObject());
    });

    Allure.step("Verify page 1 details are in response");
    assertThat(response.get().get("page").getAsInt()).as("Page").isEqualTo(1);
  }

}
