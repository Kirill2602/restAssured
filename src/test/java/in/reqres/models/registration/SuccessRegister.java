package in.reqres.models.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor
@Getter class SuccessRegister {
    private Integer id;
    private String token;
}
