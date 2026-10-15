package testdata;

import api.models.ActionType;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Map;
import java.util.stream.Stream;

import static testdata.TokenGenerator.TokenType.ONLY_DIGITS;
import static testdata.TokenGenerator.TokenType.ONLY_LETTERS;
import static testdata.TokenGenerator.TokenType.WITH_LOWERCASE;
import static testdata.TokenGenerator.TokenType.WITH_SPECIAL;
import static testdata.TokenGenerator.generate;

public final class TokenTestData {

    private TokenTestData() {
    }

    public static Stream<Arguments> invalidTokens() {
        Map<String, String> withoutToken = Map.of(
                EndpointFormParams.ACTION, ActionType.LOGIN.name()
        );

        return Stream.of(
                Arguments.of("T01: Токен длиной 31 символ",
                        EndpointFormParams.login(generate(31))),

                Arguments.of("T02: Токен длиной 33 символа",
                        EndpointFormParams.login(generate(33))),

                Arguments.of("T03: Токен со строчной буквой",
                        EndpointFormParams.login(generate(32, WITH_LOWERCASE))),

                Arguments.of("T04: Токен со спецсимволом",
                        EndpointFormParams.login(generate(32, WITH_SPECIAL))),

                Arguments.of("T05: Пустой токен",
                        EndpointFormParams.login("")),

                Arguments.of("T06: Отсутствует параметр token",
                        withoutToken),

                Arguments.of("T07: Токен только из заглавных букв",
                        EndpointFormParams.login(generate(32, ONLY_LETTERS))),

                Arguments.of("T08: Токен только из цифр",
                        EndpointFormParams.login(generate(32, ONLY_DIGITS)))
        );
    }
}
