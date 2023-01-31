package in.reqres.models.login;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


public @AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor class Login {
    @NonNull
    private String email;
    private String password;
}
