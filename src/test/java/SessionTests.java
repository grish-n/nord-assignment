import annotations.GeneratedToken;
import api.models.ResponseResult;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.models.ResponseResult.ERROR;
import static api.models.ResponseResult.OK;

@Epic("Авторизация и управление доступом")
@Feature("Управление пользовательскими сессиями")
@DisplayName("Управление пользовательскими сессиями")
class SessionTests extends BaseTest {

    @Test
    @DisplayName("T17: Завершение сессии блокирует дальнейшие действия пользователя")
    void shouldManageSessionLifecycleTest(@GeneratedToken String token) {
        steps.login(token);
        ResponseResult actionBeforeLogout = steps.actionResult(token);

        steps.logout(token);
        ResponseResult actionAfterLogout = steps.actionResult(token);

        softly.assertThat(actionBeforeLogout)
                .as("После LOGIN пользователь должен иметь доступ к ACTION")
                .isEqualTo(OK);

        softly.assertThat(actionAfterLogout)
                .as("После LOGOUT пользователь не должен иметь доступ к ACTION")
                .isEqualTo(ERROR);

        mocks.verifyAuthCalledOnceWithToken(token);
        mocks.verifyDoActionCalledOnceWithToken(token);
    }

    @Test
    @DisplayName("T18: Запрет ACTION без предварительной авторизации")
    void shouldRejectActionWithoutLoginTest(@GeneratedToken String token) {
        ResponseResult actionResult = steps.actionResult(token);

        softly.assertThat(actionResult)
                .as("ACTION без предварительного LOGIN должен быть запрещён")
                .isEqualTo(ERROR);

        mocks.verifyAuthNotCalled();
        mocks.verifyDoActionNotCalled();
    }

    @Test
    @DisplayName("T19: Авторизация одного токена не распространяется на другой")
    void shouldRejectActionWithAnotherTokenTest(
            @GeneratedToken String tokenA,
            @GeneratedToken String tokenB) {

        steps.login(tokenA);

        ResponseResult actionForTokenA = steps.actionResult(tokenA);
        ResponseResult actionForTokenB = steps.actionResult(tokenB);

        softly.assertThat(actionForTokenA)
                .as("Авторизованный токен A должен иметь доступ к ACTION")
                .isEqualTo(OK);

        softly.assertThat(actionForTokenB)
                .as("Токен B не должен наследовать авторизацию токена A")
                .isEqualTo(ERROR);

        mocks.verifyAuthCalledOnceWithToken(tokenA);
        mocks.verifyDoActionCalledWithToken(tokenA, 1);
        mocks.verifyDoActionCalledWithToken(tokenB, 0);
    }

    @Test
    @DisplayName("T20: Ошибка ACTION не должна завершать авторизацию")
    void shouldKeepSessionAfterActionFailureTest(@GeneratedToken String token) {
        steps.login(token);

        ResponseResult failedAction = steps.actionWithServiceFailure(token);
        ResponseResult repeatedAction = steps.actionResult(token);

        softly.assertThat(failedAction)
                .as("ACTION должен завершиться ошибкой при сбое внешнего сервиса")
                .isEqualTo(ERROR);

        softly.assertThat(repeatedAction)
                .as("После ошибки ACTION пользователь должен сохранить авторизацию")
                .isEqualTo(OK);

        mocks.verifyAuthCalledOnceWithToken(token);
        mocks.verifyDoActionCalledWithToken(token, 2);
    }

    @Test
    @DisplayName("T21: LOGOUT одного токена не влияет на авторизацию другого")
    void shouldLogoutOnlySpecifiedTokenTest(
            @GeneratedToken String tokenA,
            @GeneratedToken String tokenB) {

        steps.login(tokenA);
        steps.login(tokenB);
        steps.logout(tokenA);

        ResponseResult actionForTokenA = steps.actionResult(tokenA);
        ResponseResult actionForTokenB = steps.actionResult(tokenB);

        softly.assertThat(actionForTokenA)
                .as("После LOGOUT токен A должен потерять доступ")
                .isEqualTo(ERROR);

        softly.assertThat(actionForTokenB)
                .as("LOGOUT токена A не должен завершать сессию токена B")
                .isEqualTo(OK);

        mocks.verifyAuthCalledOnceWithToken(tokenA);
        mocks.verifyAuthCalledOnceWithToken(tokenB);
        mocks.verifyDoActionCalledWithToken(tokenA, 0);
        mocks.verifyDoActionCalledWithToken(tokenB, 1);
    }
}
