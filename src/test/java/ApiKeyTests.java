
import annotations.GeneratedToken;
import api.models.EndpointResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static api.models.ResponseResult.ERROR;

@Epic("Авторизация и управление доступом")
@Feature("Проверка API-ключа")
@DisplayName("Проверка API-ключа")
class ApiKeyTests extends BaseTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testdata.ApiKeyTestData#invalidApiKeys")
    @DisplayName("T13-T15: Проверка невалидного X-Api-Key")
    void shouldRejectInvalidApiKeyTest(
            String testCase,
            RequestSpecification spec,
            @GeneratedToken String token) {

        EndpointResponse response = steps.loginWithRequestSpec(token, spec);

        softly.assertThat(response.result())
                .as("Запрос с некорректным API-ключом должен быть отклонён")
                .isEqualTo(ERROR);

        softly.assertThat(response.message())
                .as("Ответ должен содержать причину отказа")
                .isNotBlank();

        mocks.verifyAuthNotCalled();
    }
}
