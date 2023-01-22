package in.reqres;

import in.models.login.Login;
import in.models.login.SuccessLogin;
import in.models.login.UnSuccessLogin;
import in.models.registration.Register;
import in.models.registration.SuccessRegister;
import in.models.registration.UnSuccessRegister;
import in.models.updateuser.Update;
import in.models.updateuser.UpdateUser;
import in.models.userdata.UserData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class RestTests {
    private static final String URL = "https://reqres.in";

    @Test
    public void checkAvatarsAndEmailTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseOK200());
        List<UserData> users = given()
                .when()
                .get("/users?page=2")
                .then()
                .log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(user -> assertTrue(user.getAvatar().contains(user.getId().toString())));
        users.forEach(user -> assertTrue(user.getEmail().contains(user.getLastName().toLowerCase())));
        assertTrue(users.stream().allMatch(user -> user.getEmail().endsWith("reqres.in")));
        assertTrue(users.stream().allMatch(user -> user.getEmail().startsWith(user.getFirstName().toLowerCase())));

    }

    @Test
    public void successRegisterTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseOK200());
        Register user = new Register("eve.holt@reqres.in", "pistol");
        String token = "QpwL5tke4Pnpja7X4";
        Integer id = 4;

        SuccessRegister successRegister = given()
                .body(user)
                .post("/register")
                .then().log().all()
                .extract().as(SuccessRegister.class);
        assertFalse(successRegister.getToken().isEmpty());
        assertNotNull(successRegister.getId());
        assertEquals(token, successRegister.getToken());
        assertEquals(id, successRegister.getId());
    }

    @Test
    public void unSuccessRegisterTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseError400());
        Register user = new Register("sydney@fife", "");
        UnSuccessRegister unSuccessRegister = given()
                .body(user)
                .post("/register")
                .then().log().all()
                .extract().as(UnSuccessRegister.class);
        assertEquals("Missing password", unSuccessRegister.getError());
    }

    @Test
    public void deleteUserTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseSpecUniqueStatus(204));
        given()
                .when()
                .delete("/users/2")
                .then().log().all();
    }

    @Test
    public void updateUserTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseOK200());
        Update update = new Update("morpheus", "zion resident");
        UpdateUser updateUser = given()
                .body(update)
                .put("/users/2")
                .then().log().all()
                .extract().as(UpdateUser.class);

        assertNotNull(updateUser.getUpdatedAt());
    }

    @Test
    public void successLoginTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseOK200());
        Login user = new Login("eve.holt@reqres.in", "cityslicka");
        SuccessLogin successfulAuthorization = given()
                .body(user)
                .post("/login")
                .then().log().all()
                .extract().as(SuccessLogin.class);

        assertFalse(successfulAuthorization.getToken().isEmpty());
    }

    @Test
    public void unSuccessLoginTest() {
        Specs.installSpecification(Specs.request(URL), Specs.responseError400());
        Login user = new Login("peter@klaven");
        UnSuccessLogin unsuccessfulAuthorization = given()
                .body(user)
                .post("/login")
                .then().log().all()
                .extract().as(UnSuccessLogin.class);

        assertEquals("Missing password", unsuccessfulAuthorization.getError());
    }
}
