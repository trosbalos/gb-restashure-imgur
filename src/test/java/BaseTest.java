import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class BaseTest extends Endpoints{
    String encodedImage;
    String uploadedImageDeleteHashCode;
    static ResponseSpecification responseSpecification = null;
    static ResponseSpecification badRequestSpec = null;
    static RequestSpecification reqSpec;
    protected static Properties prop = new Properties();
    protected static String token;
    protected static String username;
    protected static Map<String, String> headers = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = prop.getProperty("token");
        headers.put("Authorization", token);

        RestAssured.baseURI = prop.getProperty("base.url");
        username = prop.getProperty("username");
        loadProperties();

        token = prop.getProperty("token");
        username = prop.getProperty("username");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = prop.getProperty("base.url");
        RestAssured.filters(new AllureRestAssured());

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.description", is(nullValue()))
                .expectBody("data.animated", is(false))
                .expectBody("data.in_gallery", is(false))
                .expectContentType(ContentType.JSON)
                .build();


        badRequestSpec = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectStatusLine("HTTP/1.1 400 Bad Request")
                .expectContentType(ContentType.JSON)
                .build();


        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.ANY)
                .build();



    }

    private static void loadProperties() {
        try {
            prop.load(new FileInputStream("src/test/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
