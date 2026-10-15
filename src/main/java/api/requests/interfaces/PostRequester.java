package api.requests.interfaces;

import io.restassured.response.Response;

public interface PostRequester<T> {

    Response post(T data);
}
