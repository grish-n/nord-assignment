package api.requests.requesters;

import api.requests.endpoints.ApiEndpoint;
import api.requests.interfaces.PostRequester;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class FormPostRequester implements PostRequester<Map<String, String>> {

    private final RequestSpecification requestSpecification;
    private final ApiEndpoint endpoint;

    public FormPostRequester(RequestSpecification requestSpecification, ApiEndpoint endpoint) {
        this.requestSpecification = requestSpecification;
        this.endpoint = endpoint;
    }

    @Override
    public Response post(Map<String, String> formParams) {
        RequestSpecification request = given()
                .spec(requestSpecification);

        if (!formParams.isEmpty()) {
            request.formParams(formParams);
        }

        return request.post(endpoint.getPath());
    }
}
