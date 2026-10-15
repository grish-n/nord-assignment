package mocks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExternalServiceEndpoint {

    AUTH("/auth"),
    DO_ACTION("/doAction");

    private final String path;
}
