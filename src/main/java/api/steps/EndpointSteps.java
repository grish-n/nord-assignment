package api.steps;

import api.models.EndpointResponse;
import api.models.ResponseResult;
import api.requests.endpoints.ApiEndpoint;
import api.requests.requesters.FormPostRequester;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import mocks.ExternalServiceMocks;
import testdata.EndpointFormParams;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RequiredArgsConstructor
public class EndpointSteps {

    private final FormPostRequester requester;
    private final ExternalServiceMocks mocks;

    @Step("Авторизовать пользователя")
    public EndpointResponse login(String token) {
        mocks.stubAuth(SC_OK);
        return send(EndpointFormParams.login(token));
    }

    @Step("Проверить авторизацию при отказе внешнего сервиса")
    public EndpointResponse loginWithAuthFailure(String token) {
        mocks.stubAuth(SC_UNAUTHORIZED);
        return send(EndpointFormParams.login(token));
    }

    @Step("Отправить LOGIN с проверяемыми параметрами")
    public EndpointResponse loginWithFormParams(Map<String, String> formParams) {
        mocks.stubAuth(SC_OK);
        return send(formParams);
    }

    @Step("Отправить LOGIN с проверяемым API-ключом")
    public EndpointResponse loginWithRequestSpec(
            String token,
            RequestSpecification spec) {

        mocks.stubAuth(SC_OK);

        return new FormPostRequester(spec, ApiEndpoint.ENDPOINT)
                .post(EndpointFormParams.login(token))
                .as(EndpointResponse.class);
    }

    @Step("Отправить запрос с некорректным действием")
    public EndpointResponse actionWithFormParams(
            String token,
            Map<String, String> actionParams) {

        mocks.stubDoAction(SC_OK);

        Map<String, String> formParams = new HashMap<>(actionParams);
        formParams.put(EndpointFormParams.TOKEN, token);

        return send(formParams);
    }

    @Step("Выполнить действие пользователя")
    public ResponseResult actionResult(String token) {
        mocks.stubDoAction(SC_OK);
        return send(EndpointFormParams.action(token)).result();
    }

    @Step("Выполнить действие при сбое внешнего сервиса")
    public ResponseResult actionWithServiceFailure(String token) {
        mocks.stubDoAction(SC_INTERNAL_SERVER_ERROR);
        return send(EndpointFormParams.action(token)).result();
    }

    @Step("Завершить сессию пользователя")
    public void logout(String token) {
        send(EndpointFormParams.logout(token));
    }

    private EndpointResponse send(Map<String, String> formParams) {
        return requester.post(formParams).as(EndpointResponse.class);
    }
}
