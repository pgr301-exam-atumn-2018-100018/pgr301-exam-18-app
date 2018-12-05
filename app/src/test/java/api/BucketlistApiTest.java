package api;

import org.junit.Before;
import org.junit.Test;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class BucketlistApiTest
{
//    Testing:
//        1. Run Application
//        2. Maven: "spring-boot:run"
//        3. Run tests

    //setup method mainly from https://semaphoreci.com/community/tutorials/testing-rest-endpoints-using-rest-assured
    @Before
    public void setup()
    {
        //Clear map (IMDB)
        given()
                .post("/delete-all")
                .then()
                .statusCode(200)
                .extract().asString();

        String port = System.getProperty("server.port");
        if (port == null)
        {
            RestAssured.port = Integer.valueOf(8080);
        }
        else
        {
            RestAssured.port = Integer.valueOf(port);
        }


        String basePath = System.getProperty("server.base");
        if(basePath == null)
        {
            basePath = "";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if(baseHost == null)
        {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
    }

    @Test
    public void testGetEmptyList()
    {
        String res = given()
                .get("/get-list")
                .then()
                .extract().asString();

        assertEquals(res, "{}");
    }

    @Test
    public void testGetNonExistent()
    {
        given()
                .when()
                .get("/get-item/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testAddAndGetItem()
    {
        String newItem = "Buy an expensive car";
        int id = 1;

        String created = given()
                .formParam("item", newItem)
                .when()
                .post("/add-item")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(created, "true");

        String getItem = given()
                .get("get-item?id=" + id)
                .then()
                .extract().asString();

        assertEquals(getItem, newItem);
    }

    @Test
    public void testUpdateItem()
    {
        String newItem = "Buy an expensive car";
        String updateItem = "Meet a celebrity";
        int id = 1;

        String created = given()
                .formParam("item", newItem)
                .when()
                .post("/add-item")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(created, "true");

        String getItem = given()
                .get("get-item?id=" + id)
                .then()
                .extract().asString();

        assertEquals(getItem, newItem);

        String updated = given()
                .formParam("id", id)
                .formParam("item", updateItem)
                .when()
                .post("/update-item")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(updated, "true");

        String getUpdatedItem = given()
                .get("get-item?id=" + id)
                .then()
                .extract().asString();

        assertEquals(getUpdatedItem, updateItem);
    }

    @Test
    public void testDeleteOneItem()
    {
        String newItem = "Buy an expensive car";
        int id = 1;

        String created = given()
                .formParam("item", newItem)
                .when()
                .post("/add-item")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(created, "true");

        String getItem = given()
                .get("get-item?id=" + id)
                .then()
                .extract().asString();

        assertEquals(getItem, newItem);

        String deleted = given()
                .formParam("id", id)
                .when()
                .post("/delete-item")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(deleted, "true");

        String res = given()
                .get("/get-list")
                .then()
                .extract().asString();

        assertEquals(res, "{}");
    }

    @Test
    public void testClearData()
    {
        String cleared = given()
                .post("/delete-all")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(cleared, "true");
    }
}