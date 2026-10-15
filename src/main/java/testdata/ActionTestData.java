package testdata;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Map;
import java.util.stream.Stream;

public final class ActionTestData {

    private ActionTestData() {
    }

    public static Stream<Arguments> invalidActions() {
        return Stream.of(
                Arguments.of("T10: Отсутствует параметр action", Map.of()),
                Arguments.of("T11: Пустой action", Map.of(EndpointFormParams.ACTION, "")),
                Arguments.of("T12: Неизвестный action", Map.of(EndpointFormParams.ACTION, "UNKNOWN"))
        );
    }
}
