import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Base64;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class ImageTest extends BaseTest{
    ClassLoader classLoader = getClass().getClassLoader();
    File catImageJpg = new File(Objects.requireNonNull(classLoader.getResource("cat.jpg")).getFile());
    File catImageBmp = new File(Objects.requireNonNull(classLoader.getResource("cat.bmp")).getFile());
    File catImageGif= new File(Objects.requireNonNull(classLoader.getResource("cat.gif")).getFile());
    File catImagePng= new File(Objects.requireNonNull(classLoader.getResource("cat.png")).getFile());
    File catImageTxt= new File(Objects.requireNonNull(classLoader.getResource("cat.txt")).getFile());
    File catImageBigFormat= new File(Objects.requireNonNull(classLoader.getResource("cat.txtaskdjsajdifdfgergasopdfagaergaerga")).getFile());
    File byte0Image = new File(Objects.requireNonNull(classLoader.getResource("zerobyte.jpg")).getFile());
    File lilia54 = new File(Objects.requireNonNull(classLoader.getResource("lilia54.png")).getFile());

    @Test
    void imageUploadFormDataURLTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, IMAGE_URL)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;


    }
@Test
    void imageUploadFormDataURLBigResolutionTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, IMAGE_URL_BIG_RESOLUTION)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileJpgTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, catImageJpg)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileBmpTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, catImageBmp)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileGifTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, catImageGif)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFilePngTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, catImagePng)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileTxtTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, catImageTxt)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileBigFormatTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, catImageBigFormat)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileZeroTest() {
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, byte0Image)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataBase64Test() {
    EncodeToBase64 encodeToBase64=new EncodeToBase64();
    byte[] fileContent = encodeToBase64.getFileContentInBase64();
    encodedImage = Base64.getEncoder().encodeToString(fileContent);
        uploadedImageDeleteHashCode =  given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, encodedImage)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .prettyPeek()
                .jsonPath()
                .getString(DATA_DELETEHASH);;
    }

@Test
    void imageUploadFormDataFileMoreThan10mbTest() {
         given()
                .spec(reqSpec)
                .multiPart(IMAGE_KEY, lilia54)
                .expect()
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .spec(badRequestSpec);

    }

    @AfterEach
    void tearDown() {
        if(uploadedImageDeleteHashCode !=null){
        given()
                .spec(reqSpec)
                .when()
                .delete(USERNAME_IMAGE_DELETEHASH, username, uploadedImageDeleteHashCode);
            uploadedImageDeleteHashCode = null;}


    }


}
