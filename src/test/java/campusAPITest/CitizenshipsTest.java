package campusAPITest;

import campusAPITest.models.Citizenships;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CitizenshipsTest {


    public String randomCitizenshipName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String randomshortName() {
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

    Response response;
    Citizenships citizenship;

    @Test(priority = 1)
    public void createCitizenship() {

        citizenship = new Citizenships();
        citizenship.setName(randomCitizenshipName());
        citizenship.setShortName(randomshortName());

        response = given()
                .body(citizenship)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .statusCode(201)
                .log().body()
                .extract().response();

    }

    @Test(dependsOnMethods = "createCitizenship", priority = 2)
    public void createCitizenshipNegativeTest() {
        given()
                .body(citizenship)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createCitizenship", priority = 3)
    public void updateCitizenship() {

        citizenship.setId(response.jsonPath().getString("id"));
        citizenship.setName(randomCitizenshipName());
        citizenship.setShortName(randomshortName());

        given()
                .body(citizenship)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .put("/school-service/api/citizenships")
                .then()
                .statusCode(200);
    }


    @Test(dependsOnMethods = "createCitizenship", priority = 4)
    public void deleteCitizenship() {


        given()
                .pathParam("citizenshipId", response.jsonPath().getString("id"))
                .cookies(cookies)
                .when()
                .delete("school-service/api/citizenships/{citizenshipId}")
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = {"createCitizenship", "deleteCitizenship"}, priority = 5)
    public void deleteCitizenshipNegativeTest() {
        given()
                .pathParam("citizenshipId", response.jsonPath().getString("id"))
                .cookies(cookies)
                .when()
                .delete("school-service/api/citizenships/{citizenshipId}")
                .then()
                .statusCode(400);
    }


}
