import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageTests extends BaseTest{
    String encodedImage;
    String uploadedImageDeleteHashCode;

    ClassLoader classLoader = getClass().getClassLoader();
    File catImage = new File(Objects.requireNonNull(classLoader.getResource("cat.jpg")).getFile());
    File byte0Image = new File(Objects.requireNonNull(classLoader.getResource("0.jpg")).getFile());
    File lilia54 = new File(Objects.requireNonNull(classLoader.getResource("lilia54.png")).getFile());



    //Загружаем cat.jpg
    @Test
    void imageUploadFileTest() {
        uploadedImageDeleteHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", catImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

//Загружаем base64
    @Test
    void imageUploadFile64baseTest() {

        byte[] fileContent = getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);

        uploadedImageDeleteHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", encodedImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
//Загружаем 0 байт
    @Test
    void imageUploadFile0ByteTest() {
        uploadedImageDeleteHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", byte0Image)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
//Загружаем .jpg c URL
    @Test
    void imageUploadURLTest() {
        uploadedImageDeleteHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", "https://nbnews.com.ua/wp-content/uploads/2020/06/maxresdefault-7.jpg")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
//Загружаем .jpg >10mb
    @Test
    void imageUploadMoreThen10mb() {
        given()
                .headers("Authorization", token)
                .multiPart("image", lilia54)
                .expect()
                .body("success", is(false))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(400);

    }
    //Копируем изображение
    @Test
    void imageCopyByHash() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .when()
                .post("/image/8Hk0cHV")
                .prettyPeek()
                .then()
                .statusCode(200);


    }
//Добавляем в избранное
    @Test
    void imageAddToFavorite() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .when()
                .post("image/8Hk0cHV/favorite")
                .prettyPeek()
                .then()
                .statusCode(200);

    }

//Добавляем в избранное
    @Test
    void imageAddBrokenEndPoint() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(false))
                .when()
                .post("image/")
                .prettyPeek()
                .then()
                .statusCode(400);

    }

    // Updates the title or description of an image.
    @Test
    void imageUpdateTitleOfDesc() {
        given()
                .headers("Authorization", token)
                .multiPart("title", "Heart")
                .multiPart("description", "The description of the image.")
                .expect()
                .body("success", is(true))
                .when()
                .post("/image/8Hk0cHV")
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Test
    void imageDeleteByDeleteHashCode() {
        uploadedImageDeleteHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", byte0Image)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

            given()
                    .headers("Authorization", token)
                    .when()
                    .delete("account/{username}/image/{deleteHash}", username, uploadedImageDeleteHashCode)
                    .prettyPeek()
                    .then()
                    .statusCode(200);

           uploadedImageDeleteHashCode=null;

    }


    @AfterEach
    void tearDown() {
        if(uploadedImageDeleteHashCode !=null){
        given()
                .headers("Authorization", token)
                .when()
                .delete("account/{username}/image/{deleteHash}", username, uploadedImageDeleteHashCode)
                .prettyPeek()
                .then()
                .statusCode(200);
        uploadedImageDeleteHashCode = null;}
    }

    private byte[] getFileContentInBase64() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource("cat.jpg")).getFile());
        byte[] fileContent = new byte[0];
        try {
            fileContent =   FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
