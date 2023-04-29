package campusAPITest;

import Campus.Models.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    public String randomCountryName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String randomCode() {
        return RandomStringUtils.randomAlphabetic(4);
    }


    Cookies cookies;


    @BeforeClass
    public void login() {
        baseURI = "https://test.mersys.io";


        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "turkeyts");
        credentials.put("password", "TechnoStudy123");
        credentials.put("rememberMe", "true");


        cookies = given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response().getDetailedCookies();

    }


    Country country;
    String countryId;
    Response response;

    @Test
    public void createCountry() {

        country = new Country();
        country.setName(randomCountryName());
        country.setCode(randomCode());

        response = given()
                .body(country)
                .contentType(ContentType.JSON)
                .cookies(cookies)

                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().response();


    }

    @Test(dependsOnMethods = "createCountry", priority = 1)
    public void createCountryNegativeTest() {


        given()
                .body(country)
                .contentType(ContentType.JSON)
                .cookies(cookies)

                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createCountry", priority = 2)
    public void updateCountry() {

        country.setId(response.jsonPath().getString("id"));
        country.setName(randomCountryName());
        country.setCode(randomCode());

        given()
                .body(country)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200);
    }


    @Test(dependsOnMethods = "createCountry", priority = 3)
    public void deleteCountry() {

        given()
                .cookies(cookies)
                .pathParam("countryId", response.jsonPath().getString("id"))
                .when()
                .delete("/school-service/api/countries/{countryId}")
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = {"createCountry", "deleteCountry"}, priority = 4)
    public void deleteCountryNegativeTest() {

        given()
                .cookies(cookies)
                .pathParam("countryId", response.jsonPath().getString("id"))
                .when()
                .delete("/school-service/api/countries/{countryId}")
                .then()
                .statusCode(400);

    }


}
