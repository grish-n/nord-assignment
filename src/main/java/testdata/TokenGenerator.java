package testdata;

import com.github.curiousoddman.rgxgen.RgxGen;

import java.util.concurrent.ThreadLocalRandom;

public final class TokenGenerator {

    private static final String LETTERS = "[A-Z]";
    private static final String DIGITS = "[0-9]";
    private static final String ALPHANUMERIC = "[A-Z0-9]";
    private static final String REQUIRED = LETTERS + DIGITS;

    public enum TokenType {
        VALID,
        ONLY_LETTERS,
        ONLY_DIGITS,
        WITH_LOWERCASE,
        WITH_SPECIAL
    }

    private TokenGenerator() {
    }

    public static String generate(int length) {
        return generate(length, TokenType.VALID);
    }

    public static String generate(int length, TokenType type) {
        String requiredRegex = switch (type) {
            case VALID -> REQUIRED;
            case ONLY_LETTERS, ONLY_DIGITS -> "";
            case WITH_LOWERCASE -> REQUIRED + "[a-z]";
            case WITH_SPECIAL -> REQUIRED + "[@#$]";
        };

        String remainingRegex = switch (type) {
            case ONLY_LETTERS -> LETTERS;
            case ONLY_DIGITS -> DIGITS;
            default -> ALPHANUMERIC;
        };

        String required = requiredRegex.isEmpty()
                ? ""
                : RgxGen.parse(requiredRegex).generate();

        int remainingLength = length - required.length();

        if (remainingLength < 0) {
            throw new IllegalArgumentException("Недопустимая длина токена");
        }

        String remaining = remainingLength == 0
                ? ""
                : RgxGen.parse(remainingRegex + "{" + remainingLength + "}").generate();

        char[] token = (required + remaining).toCharArray();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = token.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            char temp = token[i];
            token[i] = token[j];
            token[j] = temp;
        }

        return new String(token);
    }
}
