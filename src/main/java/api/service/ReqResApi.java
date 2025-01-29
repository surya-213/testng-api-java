package api.service;

import api.ApiManager;
import api.ApiRequest;
import api.ApiResponse;
import com.google.gson.JsonElement;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReqResApi {

  private static final String BASE_URI = "https://reqres.in";

  public static ApiResponse<JsonElement> getUsers(Map<String, Object> queryParams) {
    String endPoint = "%s/api/users".formatted(BASE_URI);
    ApiRequest request = ApiRequest.builder().endpoint(endPoint).queryParams(queryParams).build();

    return ApiManager.get(request);
  }
}
