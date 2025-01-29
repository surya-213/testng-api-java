package api;

import com.google.gson.JsonElement;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents an API request with details such as the endpoint, request body, headers, parameters,
 * expected status code, and the response class type.
 */
@Getter
@Builder
public class ApiRequest {

  private String endpoint;
  private String requestBody;
  private Map<String, Object> headers;
  private Map<String, Object> queryParams;
  private Map<String, Object> pathParams;
  private Map<String, Object> formParams;

  @Builder.Default
  private Class<?> responseClass = JsonElement.class;

  @Builder.Default
  private int expectedStatusCode = 200;

  @Builder.Default
  private int attempt = 3;

  @Builder.Default
  private int attemptWait = 2;
}
