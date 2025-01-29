package api;

import java.util.Map;

public record ApiResponse<T>(Map<String, Object> headers, T body) {

}

