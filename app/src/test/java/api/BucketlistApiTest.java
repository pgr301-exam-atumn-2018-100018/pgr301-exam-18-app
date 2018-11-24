package api;

import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class BucketlistApiTest
{
    //TODO before/after clear db, test update, test delete, fix add and get
    @BeforeClass
    public static void setup()
    {
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
        String res = given().get("/get-list")
                .then().extract().asString();
        assertEquals(res, "{}");
    }

    @Test
    public void testGetNonExistent()
    {
        given().when().get("/get-item/999")
                .then().statusCode(404);
    }

    @Test
    public void testAddAndGetItem() throws Exception //FIXME gets 404
    {
        String newItem = "Buy an expensive car";
        given()
                .contentType("application/json")
                .param("item", newItem)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .post("/add-item").then()
                .statusCode(200);


        String s = given().get("get-item/1").then().extract().asString();
        String s1 = given().get("get-item/0").then().extract().asString();
        String s2 = given().get("get-item/2").then().extract().asString();
        System.out.println("string: " + s);
        System.out.println("string1: " + s1);
        System.out.println("string2: " + s2);
    }

//    @Test
//    public void test() throws Exception
//    {
//        String s = given().get().then().extract().asString();
//        System.out.println(s);
//    }
}