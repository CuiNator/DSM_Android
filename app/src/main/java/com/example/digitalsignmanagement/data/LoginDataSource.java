package com.example.digitalsignmanagement.data;

import android.widget.Toast;

import com.example.digitalsignmanagement.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        String DummyUsername ="Alex";
        String DummyPassword ="test123";
        try {
            if (username == DummyUsername && password == DummyPassword) {
                // TODO: handle loggedInUser authentication
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Jane Doe");
                return new Result.Success<>(fakeUser);

            }else{
                return new Result.Error(new IOException("Error logging in", null));

                }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}