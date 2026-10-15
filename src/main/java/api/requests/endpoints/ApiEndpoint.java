package api.requests.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiEndpoint {

    ENDPOINT("/endpoint");

    private final String path;
}
