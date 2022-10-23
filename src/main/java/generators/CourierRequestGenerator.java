package generators;

import org.apache.commons.lang3.RandomStringUtils;
import pojos.CourierRequest;

public class CourierRequestGenerator {
    public static CourierRequest getRandomCourierRequest() {
        CourierRequest courierRequest = new CourierRequest(RandomStringUtils.randomAlphabetic(10), "password", "fistname");
        return courierRequest;
    }
}
