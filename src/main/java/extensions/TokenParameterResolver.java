package extensions;

import annotations.GeneratedToken;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import testdata.TokenGenerator;

public class TokenParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext) {

        return parameterContext.isAnnotated(GeneratedToken.class)
                && parameterContext.getParameter().getType() == String.class;
    }

    @Override
    public Object resolveParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext) {

        return TokenGenerator.generate(32);
    }
}
