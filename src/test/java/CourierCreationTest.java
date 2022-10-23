import entities.CourierEntity;
import generators.LoginRequestGenerator;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.CourierRequest;
import pojos.LoginRequest;
import pojos.DeleteRequest;


import static generators.CourierRequestGenerator.getRandomCourierRequest;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest {
    private CourierEntity randomCourier;
    private Integer id;

    @Before
    public void setUp() {
        randomCourier = new CourierEntity();
    }

    @Test
    public void SuccessfulCourierCreation() {

        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        id = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    public void TwoSimilarCouriersCreationIsNotAllowed() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        id = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    public void CourierCreateWithoutLogin() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourierRequest.setLogin(null);
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void CourierCreateWithoutPassword() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourierRequest.setPassword(null);
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (id != null) {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.setId(id);
            randomCourier.delete(deleteRequest)
                            .assertThat()
                                    .body("ok", equalTo(true));
        }

    }

}
