package generators;

import org.apache.commons.lang3.RandomStringUtils;
import pojos.CreateOrderRequest;

public class OrderRequestGenerator {
    public static CreateOrderRequest getRandomOrderRequest() {
        Integer year = (int)(Math.random() * 2) + 2022;
        Integer day = (int)(Math.random() * 30) + 1;
        Integer month = (int)(Math.random() * 11) + 1;

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setFirstName(RandomStringUtils.randomAlphabetic(15));
        createOrderRequest.setLastName(RandomStringUtils.randomAlphabetic(15));
        createOrderRequest.setAddress(RandomStringUtils.randomAlphabetic(20));
        createOrderRequest.setMetroStation(RandomStringUtils.randomNumeric(10));
        createOrderRequest.setRentTime((int) (Math.random() * 2));
        createOrderRequest.setDeliveryDate(year.toString() + '-' + month.toString() + '-' + day.toString());
        createOrderRequest.setComment(RandomStringUtils.randomAlphabetic(30));
        createOrderRequest.setPhone(RandomStringUtils.randomNumeric(10));
        createOrderRequest.setColor(new String[]{""});
        return createOrderRequest;
    }
}
