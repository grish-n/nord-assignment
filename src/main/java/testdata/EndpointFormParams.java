package testdata;

import api.models.ActionType;

import java.util.Map;

public final class EndpointFormParams {

    public static final String ACTION = "action";
    public static final String TOKEN = "token";

    private EndpointFormParams() {
    }

    public static Map<String, String> login(String token) {
        return create(ActionType.LOGIN, token);
    }

    public static Map<String, String> action(String token) {
        return create(ActionType.ACTION, token);
    }

    public static Map<String, String> logout(String token) {
        return create(ActionType.LOGOUT, token);
    }

    private static Map<String, String> create(ActionType action, String token) {
        return Map.of(
                ACTION, action.name(),
                TOKEN, token
        );
    }
}
