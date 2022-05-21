import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginEndpointTest {

    String loginEndpoint = "https://ws-test.keepit.com/users/";
    String userId = "zhc4v6-5ev7di-9hhhlm";
    String login = "automation@keepitqa.com";
    String password = "E#*b2wGIbFHz";


    @Test
    @Order(1)
    void verifyIfUserIsAbleToLogin() {
        // Given
        var specResponse = given().accept(ContentType.XML).auth().basic(login, password).when();

        // When
        var response = specResponse.get(loginEndpoint + userId).then();

        // Then
        response.assertThat().statusCode(200);
    }

    @Test
    @Order(2)
    void verifyLoginXMLBodyResponse() {
        // Given
        var specResponse = given().accept(ContentType.XML).auth().basic(login, password).when();

        // When
        var response = specResponse.get(loginEndpoint + userId).then();

        // Then
        XmlPath responseXml = response.extract().body().xmlPath();
        var userElements = response.extract().body().xmlPath().get().children().list().stream().map(Node::name).collect(Collectors.toList());

        assertAll(
                () -> assertTrue(CommonHelpers.isBoolean(responseXml.get("user.enabled")), "user.enabled is not boolean type"),
                () -> assertTrue(CommonHelpers.isValidISODateTime(responseXml.get("user.created")), "user.created is not ISO 8601 datetime format"),
                () -> assertTrue(CommonHelpers.isBoolean(responseXml.get("user.subscribed")), "user.subscribed is not boolean type"),

                // optional parameters validation
                () -> assertTrue(!userElements.contains("user.product") ||
                        responseXml.get("user.product") instanceof String, "user.product is not String object"),
                () -> assertTrue(!userElements.contains("user.parent") ||
                        responseXml.get("user.parent") instanceof String, "user.parent is not String object"),
                () -> assertTrue(!userElements.contains("user.deletion-deadline") ||
                        CommonHelpers.isValidISODateTime(responseXml.get("user.deletion-deadline")), "user.deletion-deadline is not ISO 8601 datetime format"),
                () -> assertTrue(!userElements.contains("user.external_id") ||
                        responseXml.get("user.external_id") instanceof String, "user.external_id is not String object")
        );
    }
}
