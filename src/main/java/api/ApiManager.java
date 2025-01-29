package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exceptions.TestCaseException;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import utils.PauseUtil;

/**
 * Utility class for making API requests using Rest Assured.
 */
@Slf4j
@UtilityClass
public class ApiManager {

  private final Gson gson = new Gson();

  /**
   * Sends a GET request.
   */
  public <T> T get(ApiRequest request) {
    return execute(request, "GET");
  }

  /**
   * Sends a POST request.
   */
  public <T> T post(ApiRequest request) {
    return execute(request, "POST");
  }

  /**
   * Sends a DELETE request.
   */
  public <T> T delete(ApiRequest request) {
    return execute(request, "DELETE");
  }

  /**
   * Sends a PUT request.
   */
  public <T> T put(ApiRequest request) {
    return execute(request, "PUT");
  }

  /**
   * Sends a PATCH request.
   */
  public <T> T patch(ApiRequest request) {
    return execute(request, "PATCH");
  }

  /**
   * Executes an HTTP request with a retry mechanism.
   */
  @SuppressWarnings("unchecked")
  private <T> T execute(ApiRequest request, String httpMethod) {
    int attempts = 0;
    Response response = null;

    while (attempts < request.getAttempt()) {
      response = sendRequest(request, httpMethod);

      if (response.getStatusCode() == request.getExpectedStatusCode()) {
        return (T) handleResponse(response, request.getResponseClass());
      }

      attempts++;

      if (attempts < request.getAttempt()) {
        PauseUtil.forDuration(TimeUnit.SECONDS, request.getAttemptWait());
      }
    }

    throw new TestCaseException(String.format(
        "Max retry attempts (%d) reached for request: %s. Expected status code: %d, but received: %d.",
        request.getAttempt(), request.getEndpoint(), request.getExpectedStatusCode(),
        Objects.requireNonNull(
            response).getStatusCode()
    ));
  }


  /**
   * Sends an HTTP request based on the method.
   */
  private Response sendRequest(ApiRequest request, String httpMethod) {
    RequestSpecification requestSpec = RestAssured.given()
        .filter(new AllureRestAssured())
        .baseUri(request.getEndpoint());
    applyHeaders(requestSpec, request.getHeaders());
    applyQueryParams(requestSpec, request.getQueryParams());
    applyFormParams(requestSpec, request.getFormParams());
    applyRequestBody(requestSpec, request.getRequestBody());
    applyPathParams(requestSpec, request.getPathParams());

    return switch (httpMethod.toUpperCase()) {
      case "GET" -> requestSpec.get();
      case "POST" -> requestSpec.post();
      case "DELETE" -> requestSpec.delete();
      case "PUT" -> requestSpec.put();
      case "PATCH" -> requestSpec.patch();
      default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
    };
  }

  /**
   * Processes and deserializes the HTTP response.
   */
//  private <T> T handleResponse(Response response, Class<T> responseClass) {
//    return gson.fromJson(response.asString(), responseClass);
//  }
  private <T> ApiResponse<T> handleResponse(Response response, Class<T> responseClass) {
    Map<String, Object> headersMap = response.getHeaders().asList().stream()
        .collect(Collectors.toMap(Header::getName, Header::getValue));

    // Parse response body into responseClass
    T responseBody = gson.fromJson(response.asString(), responseClass);

    // Return as ApiResponse object
    return new ApiResponse<>(headersMap, responseBody);
  }

  private void applyHeaders(RequestSpecification requestSpec, Map<String, Object> headers) {
    Optional.ofNullable(headers)
        .filter(reqHeaders -> !reqHeaders.isEmpty())
        .ifPresent(requestSpec::headers);
  }

  private void applyQueryParams(RequestSpecification requestSpec, Map<String, Object> queryParams) {
    Optional.ofNullable(queryParams)
        .filter(params -> !params.isEmpty())
        .ifPresent(requestSpec::queryParams);
  }

  private void applyFormParams(RequestSpecification requestSpec, Map<String, Object> formParams) {
    Optional.ofNullable(formParams)
        .filter(params -> !params.isEmpty())
        .ifPresent(requestSpec::formParams);
  }

  private void applyRequestBody(RequestSpecification requestSpec, String requestBody) {
    Optional.ofNullable(requestBody).ifPresent(requestSpec::body);
  }

  private void applyPathParams(RequestSpecification requestSpec, Map<String, Object> pathParams) {
    Optional.ofNullable(pathParams)
        .filter(params -> !params.isEmpty())
        .ifPresent(requestSpec::pathParams);
  }
}
