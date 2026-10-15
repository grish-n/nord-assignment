package mocks;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.RequiredArgsConstructor;
import testdata.EndpointFormParams;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
public class ExternalServiceMocks {

    private final WireMockExtension wireMock;

    public void stubAuth(int statusCode) {
        stubPost(ExternalServiceEndpoint.AUTH, statusCode);
    }

    public void stubDoAction(int statusCode) {
        stubPost(ExternalServiceEndpoint.DO_ACTION, statusCode);
    }

    public void verifyAuthCalledOnceWithToken(String token) {
        verifyAuthCalledWithToken(token, 1);
    }

    public void verifyAuthCalledWithToken(String token, int expectedCalls) {
        verifyPostWithToken(ExternalServiceEndpoint.AUTH, token, expectedCalls);
    }

    public void verifyAuthNotCalled() {
        verifyPostNotCalled(ExternalServiceEndpoint.AUTH);
    }

    public void verifyDoActionNotCalled() {
        verifyPostNotCalled(ExternalServiceEndpoint.DO_ACTION);
    }

    public void verifyDoActionCalledWithToken(String token, int expectedCalls) {
        verifyPostWithToken(ExternalServiceEndpoint.DO_ACTION, token, expectedCalls);
    }

    public void verifyDoActionCalledOnceWithToken(String token) {
        verifyDoActionCalledWithToken(token, 1);
    }

    private void stubPost(ExternalServiceEndpoint endpoint, int statusCode) {
        wireMock.stubFor(
                post(urlEqualTo(endpoint.getPath()))
                        .willReturn(aResponse().withStatus(statusCode))
        );
    }

    private void verifyPostWithToken(ExternalServiceEndpoint endpoint, String token, int expectedCalls) {
        wireMock.verify(
                expectedCalls,
                postRequestedFor(urlEqualTo(endpoint.getPath()))
                        .withRequestBody(equalTo(EndpointFormParams.TOKEN + "=" + token))
        );
    }

    private void verifyPostNotCalled(ExternalServiceEndpoint endpoint) {
        wireMock.verify(0, postRequestedFor(urlEqualTo(endpoint.getPath())));
    }
}
