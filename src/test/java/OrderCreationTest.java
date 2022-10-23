import entities.OrderEntity;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojos.DeleteRequest;
import pojos.CreateOrderRequest;

import static generators.OrderRequestGenerator.getRandomOrderRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final String[] color;

    public OrderCreationTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{""}}
        };
    }

    @Test
    public void SuccessfulOrderCreation() {
        CreateOrderRequest randomCreateOrderRequest = getRandomOrderRequest();
        randomCreateOrderRequest.setColor(color);
        OrderEntity randomOrder = new OrderEntity();
        Integer track = randomOrder.create(randomCreateOrderRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("track", notNullValue())
                .extract()
                .path("track");
        cancelOrder(track, randomOrder);
    }

    public void cancelOrder(Integer track, OrderEntity randomOrder) {
        if (track != null) {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.setId(track);
            randomOrder.cancel(deleteRequest)
                    .assertThat()
                    .body("ok", equalTo(true));
        }

    }

}
