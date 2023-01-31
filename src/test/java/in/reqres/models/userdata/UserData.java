package in.reqres.models.userdata;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

public @Getter
@AllArgsConstructor class UserData {
    private Integer id;
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;
    private String avatar;
}
