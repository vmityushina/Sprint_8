package entities;

import io.restassured.response.ValidatableResponse;
import pojos.DeleteRequest;
import pojos.CreateOrderRequest;
import pojos.GetOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderEntity extends RestEntity {
    public ValidatableResponse create(CreateOrderRequest createOrderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(createOrderRequest)
                .post("/api/v1/orders")
                .then();
    }

    public ValidatableResponse cancel(DeleteRequest deleteRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(deleteRequest)
                .queryParam("track", deleteRequest.getId())
                .put("/api/v1/orders/cancel")
                .then();
    }

    public ValidatableResponse getList(GetOrderRequest getOrderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(getOrderRequest)
                .queryParam("courierId", getOrderRequest.getCourierId().toString())
                .get("/api/v1/orders")
                .then();
    }

    public ValidatableResponse trackRequest(GetOrderRequest getOrderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .queryParam("t", getOrderRequest.getTrack().toString())
                .get("/api/v1/orders/track")
                .then();
    }

    public ValidatableResponse acceptOrder(GetOrderRequest getOrderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .queryParam("courierId", getOrderRequest.getCourierId().toString())
                .put("/api/v1/orders/accept/{id}", getOrderRequest.getId().toString())
                .then();
    }

    public ValidatableResponse finishOrder(DeleteRequest deleteRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .put("api/v1/orders/finish/{id}", deleteRequest.getId())
                .then();
    }
}
