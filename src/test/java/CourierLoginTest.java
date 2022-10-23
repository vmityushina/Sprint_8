import entities.CourierEntity;
import generators.LoginRequestGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.CourierRequest;
import pojos.DeleteRequest;
import pojos.LoginRequest;

import static generators.CourierRequestGenerator.getRandomCourierRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private CourierEntity randomCourier;
    private Integer id;

    @Before
    public void setUp() {
        randomCourier = new CourierEntity();
    }

    @Test
    public void SuccessfulCourierLogin() {
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
    //some fancy method name
    public void LoginWithoutLogin() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        //getting id to delete on teardown
        id = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        //set login=null to check status code and message
        loginRequest.setLogin(null);
        randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void LoginWithoutPassword() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        //getting id to delete on teardown
        id = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        //here I get statusCode=504 instead of 400. It is 400 in spec, so leaving 400 in assert
        loginRequest.setPassword(null);
        randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void LoginWithWrongCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        //setting random values to simulate that wrong login/passw entered
        loginRequest.setLogin(RandomStringUtils.randomAlphabetic(10));
        loginRequest.setPassword(RandomStringUtils.randomAlphabetic(5));

        randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    //user exists, but they doesn't remember their password
    public void LoginWithRightLoginAndWrongPassword() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        String realPassword = RandomStringUtils.randomAlphabetic(10);
        randomCourierRequest.setPassword(realPassword);
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        //getting id to delete on teardown
        id = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        //changing password to simulate fat finger typo
        loginRequest.setPassword(realPassword + RandomStringUtils.randomAlphabetic(3));
        randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    //that's my interpretation of non-existent user
    public void LoginWithRightPasswordAndWrongLogin() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        String realLogin = RandomStringUtils.randomAlphabetic(10);
        randomCourierRequest.setLogin(realLogin);
        randomCourier.create(randomCourierRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        //getting id to delete on teardown
        id = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        //changing login to simulate non-existent user who tries to login using someone else's password
        loginRequest.setLogin(realLogin + RandomStringUtils.randomAlphabetic(3));
        randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
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
