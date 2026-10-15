package api.models;

public record EndpointResponse(
        ResponseResult result,
        String message
) {
}
