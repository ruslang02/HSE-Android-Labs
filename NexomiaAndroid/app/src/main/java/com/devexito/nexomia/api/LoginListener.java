package com.devexito.nexomia.api;

public interface LoginListener {
    void onSuccess(String token);
    void onEmailVerify();
    void onError(String message);
}
