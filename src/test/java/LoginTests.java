
import annotations.GeneratedToken;
import api.models.EndpointResponse;
import api.models.ResponseResult;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.models.ResponseResult.ERROR;

@Epic("Авторизация и управление доступом")
@Feature("Авторизация")
@DisplayName("Проверка авторизации")
class LoginTests extends BaseTest {

    @Test
    @DisplayName("T16: Отказ внешнего сервиса не создаёт авторизованную сессию")
    void shouldRejectLoginWhenAuthServiceDeniesAccessTest(@GeneratedToken String token) {
        EndpointResponse loginResponse = steps.loginWithAuthFailure(token);
        ResponseResult actionResult = steps.actionResult(token);

        softly.assertThat(loginResponse.result())
                .as("LOGIN должен завершиться ошибкой при отказе /auth")
                .isEqualTo(ERROR);

        softly.assertThat(actionResult)
                .as("После отказа /auth токен не должен получить доступ к ACTION")
                .isEqualTo(ERROR);

        mocks.verifyAuthCalledOnceWithToken(token);
        mocks.verifyDoActionNotCalled();
    }
}
