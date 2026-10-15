
import annotations.GeneratedToken;
import api.models.EndpointResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

import static api.models.ResponseResult.ERROR;
import static api.models.ResponseResult.OK;

@Epic("Авторизация и управление доступом")
@Feature("Валидация токена")
@DisplayName("Проверка формата токенов")
class TokenValidationTests extends BaseTest {

    @Issue("BUG-1")
    @Description("""
        Приложение пропускает проверку токенов, состоящих только из букв или только из цифр,
        и отправляет запрос во внешний сервис /auth.

        Ожидается отклонение таких токенов без обращения к /auth.
        Требуется уточнить, обязательно ли наличие и букв, и цифр в токене.
        """)
    @ParameterizedTest(name = "{0}")
    @MethodSource("testdata.TokenTestData#invalidTokens")
    @DisplayName("T01-T08: Проверка невалидных токенов")
    void shouldRejectInvalidTokenTest(String testCase, Map<String, String> formParams) {
        EndpointResponse response = steps.loginWithFormParams(formParams);

        softly.assertThat(response.result())
                .as("Некорректный токен должен быть отклонён")
                .isEqualTo(ERROR);

        softly.assertThat(response.message())
                .as("Ответ должен содержать причину отказа")
                .isNotBlank();

        mocks.verifyAuthNotCalled();
    }

    @Test
    @Issue("BUG-2")
    @Description("""
        Приложение отклоняет валидные токены, содержащие буквы G-Z.

        По требованиям допустимы 32 символа из диапазона A-Z и 0-9,
        однако приложение принимает только символы A-F и цифры 0-9.

        Ожидается успешная авторизация с токеном, соответствующим требованиям.
        """)
    @DisplayName("T09: Успешная авторизация с валидным токеном")
    void shouldAcceptValidLengthTokenTest(@GeneratedToken String token) {
        EndpointResponse response = steps.login(token);

        softly.assertThat(response.result())
                .as("LOGIN с валидным токеном должен завершиться успешно")
                .isEqualTo(OK);

        mocks.verifyAuthCalledOnceWithToken(token);
    }
}
