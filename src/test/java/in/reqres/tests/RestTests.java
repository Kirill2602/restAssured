package in.reqres.tests;

import in.reqres.models.login.Login;
import in.reqres.models.login.SuccessLogin;
import in.reqres.models.login.UnSuccessLogin;
import in.reqres.models.registration.Register;
import in.reqres.models.registration.SuccessRegister;
import in.reqres.models.registration.UnSuccessRegister;
import in.reqres.models.updateuser.Update;
import in.reqres.models.updateuser.UpdateUser;
import in.reqres.models.userdata.UserData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static in.reqres.endpoints.EndPoints.*;
import static in.reqres.specs.Specs.requestSpec;
import static in.reqres.specs.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

public class RestTests {
    int pageNumber = 2,
            userId = 2;

    @Test
    public void checkAvatarsAndEmailTest() {
        List<UserData> users = given(requestSpec)
                .when()
                .get(GET_USERS_LIST_ENDPOINT, pageNumber)
                .then()
                .spec(responseSpec(200))
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(user -> assertTrue(user.getAvatar().contains(user.getId().toString())));
        users.forEach(user -> assertTrue(user.getEmail().contains(user.getLastName().toLowerCase())));
        assertTrue(users.stream().allMatch(user -> user.getEmail().endsWith("reqres.in")));
        assertTrue(users.stream().allMatch(user -> user.getEmail().startsWith(user.getFirstName().toLowerCase())));

    }

    @Test
    public void successRegisterTest() {
        Register user = new Register("eve.holt@reqres.in", "pistol");
        String token = "QpwL5tke4Pnpja7X4";
        int id = 4;

        SuccessRegister successRegister = given(requestSpec)
                .body(user)
                .post(REGISTRATION_ENDPOINT)
                .then()
                .spec(responseSpec(200))
                .extract().as(SuccessRegister.class);

        assertFalse(successRegister.getToken().isEmpty());
        assertNotNull(successRegister.getId());
        assertEquals(token, successRegister.getToken());
        assertEquals(id, successRegister.getId());
    }

    @Test
    public void updateUserTest() {
        Update updateData = new Update("morpheus", "zion resident");
        UpdateUser updateUser = given(requestSpec)
                .body(updateData)
                .put(UPDATE_USER_ENDPOINT, userId)
                .then()
                .spec(responseSpec(200))
                .extract().as(UpdateUser.class);
        assertNotNull(updateUser.getUpdatedAt());
    }

    @Test
    public void successLoginTest() {
        Login user = new Login("eve.holt@reqres.in", "cityslicka");
        SuccessLogin successfulAuthorization = given(requestSpec)
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(responseSpec(200))
                .extract().as(SuccessLogin.class);

        assertFalse(successfulAuthorization.getToken().isEmpty());
    }

    @Test
    public void resourcesListWithGrooveTest() {
        given(requestSpec)
                .when()
                .get(RESOURCES_ENDPOINT)
                .then()
                .spec(responseSpec(200))
                .body("data.findAll{it.id == 1}.name", hasItem("cerulean"))
                .body("data.findAll{it.id == 4}.color", hasItem("#7BC4C4"))
                .body("data.findAll{it.id == 6}.year", hasItem(2005))
                .body("support", hasKey("url"))
                .body("support", hasKey("text"));
    }

    @Test
    public void unSuccessRegisterTest() {
        Register user = new Register("sydney@fife", "");
        UnSuccessRegister unSuccessRegister = given(requestSpec)
                .body(user)
                .post(REGISTRATION_ENDPOINT)
                .then()
                .spec(responseSpec(400))
                .extract().as(UnSuccessRegister.class);

        assertEquals("Missing password", unSuccessRegister.getError());
    }

    @Test
    public void unSuccessLoginTest() {
        Login user = new Login("peter@klaven");
        UnSuccessLogin unsuccessfulAuthorization = given(requestSpec)
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(responseSpec(400))
                .extract().as(UnSuccessLogin.class);

        assertEquals("Missing password", unsuccessfulAuthorization.getError());
    }

    @Test
    public void deleteUserTest() {
        given(requestSpec)
                .when()
                .delete(DELETE_USER_ENDPOINT, userId)
                .then()
                .spec(responseSpec(204));
    }
}
