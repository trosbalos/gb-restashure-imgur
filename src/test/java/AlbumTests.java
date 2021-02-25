import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AlbumTests  extends BaseTest{
    String albumDeleteHash;
    @Test
    void createAlbumTest() {
        albumDeleteHash = given()
                .spec(reqSpec)
                .formParam("title",ALBUM_NAME)
                .log()
                .all()
                .when()
                .post(ALBUM)
                .prettyPeek()
                .then()
                .spec(responseSpecification)

                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");;
    }

    @AfterEach
    void tearDown() {
        if(albumDeleteHash !=null){
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(USERNAME_IMAGE_DELETEHASH, username, albumDeleteHash)
                    .prettyPeek()
                    .then()
                    .spec(responseSpecification);
            albumDeleteHash = null;}
    }
}
