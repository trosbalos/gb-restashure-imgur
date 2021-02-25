import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
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
                .spec(reqSpec)
                .multiPart("image", catImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

//Загружаем base64
    @Test
    void imageUploadFile64baseTest() {
        EncodeToBase64 encodeToBase64=new EncodeToBase64();
        byte[] fileContent = encodeToBase64.getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);



        uploadedImageDeleteHashCode = given()
                .spec(reqSpec)
                .multiPart("image", encodedImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
//Загружаем 0 байт
    @Test
    void imageUploadFile0ByteTest() {
        uploadedImageDeleteHashCode = given()
                .spec(reqSpec)
                .multiPart("image", byte0Image)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
//Загружаем .jpg c URL
    @Test
    void imageUploadURLTest() {
        uploadedImageDeleteHashCode = given()
                .spec(reqSpec)
                .multiPart("image", IMAGE_URL)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
//Загружаем .jpg >10mb
    @Test
    void imageUploadMoreThen10mb() {
        given()
                .spec(reqSpec)
                .multiPart("image", lilia54)
                .expect()
                .body("success", is(false))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badRequestSpec);

    }
    //Копируем изображение
    @Test
    void imageCopyByHash() {
        given()
                .spec(reqSpec)
                .expect()
                .body("success", is(true))
                .when()
                .post(FAVORITE_IMAGE)
                .prettyPeek()
                .then()
                .spec(responseSpecification); // ТЕСТ КОДА СПЕЦИФИКАЦИИ


    }
//Добавляем в избранное
    @Test
    void imageAddToFavorite() {
        given()
                .spec(reqSpec)
                .expect()
                .body("success", is(true))
                .when()
                .post(FAVORITE_IMAGE)
                .prettyPeek()
                .then()
                .spec(responseSpecification);

    }

//Добавляем в избранное
    @Test
    void imageAddBrokenEndPoint() {
        given()
                .spec(reqSpec)
                .expect()
                .body("success", is(false))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badRequestSpec);

    }

    // Updates the title or description of an image.
    @Test
    void imageUpdateTitleAndDesc() {
        given()
                .spec(reqSpec)
                .multiPart("title", "Heart")
                .multiPart("description", "The description of the image.")
                .expect()
                .body("success", is(true))
                .when()
                .post(UPDATE_IMAGE_INFO)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }
    @Test
    void imageDeleteByDeleteHashCode() {
        uploadedImageDeleteHashCode = given()
                .spec(reqSpec)
                .multiPart("image", byte0Image)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

            given()
                    .spec(reqSpec)
                    .when()
                    .delete(USERNAME_IMAGE_DELETEHASH, username, uploadedImageDeleteHashCode)
                    .prettyPeek()
                    .then()
                    .spec(responseSpecification);

           uploadedImageDeleteHashCode=null;

    }


    @AfterEach
    void tearDown() {
        if(uploadedImageDeleteHashCode !=null){
        given()
                .spec(reqSpec)
                .when()
                .delete(USERNAME_IMAGE_DELETEHASH, username, uploadedImageDeleteHashCode)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
        uploadedImageDeleteHashCode = null;}
    }

}
