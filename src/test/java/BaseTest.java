
import api.requests.endpoints.ApiEndpoint;
import api.requests.requesters.FormPostRequester;
import api.specs.RequestSpecs;
import api.steps.EndpointSteps;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import config.Config;
import extensions.TokenParameterResolver;
import mocks.ExternalServiceMocks;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URI;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ExtendWith(TokenParameterResolver.class)
public abstract class BaseTest {

    protected ExternalServiceMocks mocks;
    protected EndpointSteps steps;

    @RegisterExtension
    protected static final WireMockExtension wireMock =
            WireMockExtension.newInstance()
                    .options(wireMockConfig()
                            .port(URI.create(Config.wireMockUrl()).getPort()))
                    .build();

    protected SoftAssertions softly;
    protected FormPostRequester requester;

    @BeforeEach
    void setUp() {
        softly = new SoftAssertions();

        requester = new FormPostRequester(
                RequestSpecs.authSpec(),
                ApiEndpoint.ENDPOINT
        );

        mocks = new ExternalServiceMocks(wireMock);

        steps = new EndpointSteps(requester, mocks);
    }

    @AfterEach
    void tearDown() {
        softly.assertAll();
    }
}
