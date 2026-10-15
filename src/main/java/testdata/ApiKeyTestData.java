package testdata;

import api.specs.RequestSpecs;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class ApiKeyTestData {

    private ApiKeyTestData() {
    }

    public static Stream<Arguments> invalidApiKeys() {
        return Stream.of(
                Arguments.of("T13: Отсутствует X-Api-Key", RequestSpecs.unauthSpec()),
                Arguments.of("T14: Пустой X-Api-Key", RequestSpecs.withApiKey("")),
                Arguments.of("T15: Неверный X-Api-Key", RequestSpecs.withApiKey("invalid-key"))
        );
    }
}
