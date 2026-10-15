import annotations.GeneratedToken;
import api.models.EndpointResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

import static api.models.ResponseResult.ERROR;

@Epic("Авторизация и управление доступом")
@Feature("Валидация действия")
@DisplayName("Проверка допустимых действий")
class ActionValidationTests extends BaseTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testdata.ActionTestData#invalidActions")
    @DisplayName("T10-T12: Проверка невалидных значений action")
    void shouldRejectInvalidActionTest(
            String testCase,
            Map<String, String> actionParams,
            @GeneratedToken String token) {

        steps.login(token);

        EndpointResponse response = steps.actionWithFormParams(token, actionParams);

        softly.assertThat(response.result())
                .as("Некорректный action должен быть отклонён")
                .isEqualTo(ERROR);

        softly.assertThat(response.message())
                .as("Ответ должен содержать причину отказа")
                .isNotBlank();

        mocks.verifyAuthCalledOnceWithToken(token);
        mocks.verifyDoActionNotCalled();
    }
}
