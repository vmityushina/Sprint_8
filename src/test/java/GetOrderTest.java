import entities.CourierEntity;
import entities.OrderEntity;
import generators.LoginRequestGenerator;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.*;

import static generators.CourierRequestGenerator.getRandomCourierRequest;
import static generators.OrderRequestGenerator.getRandomOrderRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {
    private OrderEntity randomOrder;
    private CourierEntity randomCourier;
    private Integer courierId;
    private Integer track;
    private Integer orderId;

    @Before
    public void setUp() {
        randomCourier = new CourierEntity();
        randomOrder = new OrderEntity();
    }

    @Test
    public void GetAllCouriersOrders() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        randomCourier.create(randomCourierRequest);
        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        courierId = randomCourier.login(loginRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("id");
        CreateOrderRequest randomCreateOrderRequest = getRandomOrderRequest();
        track = randomOrder.create(randomCreateOrderRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("track");

        GetOrderRequest getOrderRequest = new GetOrderRequest();
        getOrderRequest.setTrack(track);
        getOrderRequest.setCourierId(courierId);
        orderId = randomOrder.trackRequest(getOrderRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("order.id");

        getOrderRequest.setId(orderId);
        randomOrder.acceptOrder(getOrderRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        randomOrder.getList(getOrderRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("orders", notNullValue());
    }

    @After
    public void tearDown() {
        DeleteRequest deleteRequest = new DeleteRequest();
        if (orderId != null) {
            deleteRequest.setId(orderId);
            randomOrder.finishOrder(deleteRequest)
                    .assertThat()
                    .body("ok", equalTo(true));
        }
        if (courierId != null) {
            deleteRequest.setId(courierId);
            randomCourier.delete(deleteRequest)
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }
}
