package in.reqres.helpers;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;

public class CustomApiListener {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    public static Filter withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;
    }
}
