package api.specs;

import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecs {

    private static final String API_KEY_HEADER = "X-Api-Key";

    private RequestSpecs() {
    }


    private static RequestSpecBuilder defaultRequestBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.baseUrl())
                .setContentType(ContentType.URLENC)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());
    }


    public static RequestSpecification authSpec() {
        return withApiKey(Config.apiKey());
    }

    public static RequestSpecification unauthSpec() {
        return defaultRequestBuilder().build();
    }

    public static RequestSpecification withApiKey(String apiKey) {
        return defaultRequestBuilder()
                .addHeader(API_KEY_HEADER, apiKey)
                .build();
    }
}
