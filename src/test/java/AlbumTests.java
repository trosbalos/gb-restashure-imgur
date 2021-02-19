import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AlbumTests  extends BaseTest{
    String albumDeleteHash;
    @Test
    void createAlbumTest() {
        albumDeleteHash = given()
                .headers(headers)
                .formParam("title","test 66666")
                .log()
                .all()
                .when()
                .post("https://api.imgur.com/3/album")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");;
    }

    @AfterEach
    void tearDown() {
        if(albumDeleteHash !=null){
            given()
                    .headers("Authorization", token)
                    .when()
                    .delete("account/{username}/image/{deleteHash}", username, albumDeleteHash)
                    .prettyPeek()
                    .then()
                    .statusCode(200);
            albumDeleteHash = null;}
    }
}
