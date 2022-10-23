package entities;

import io.restassured.response.ValidatableResponse;
import pojos.CourierRequest;
import pojos.DeleteRequest;
import pojos.LoginRequest;

import static io.restassured.RestAssured.given;

public class CourierEntity extends RestEntity{
    //create
    public ValidatableResponse create(CourierRequest courierRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(courierRequest)
                .post( "/api/v1/courier")
                .then();
    }

    //login
    public ValidatableResponse login(LoginRequest loginRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequest)
                .post("/api/v1/courier/login")
                .then();
    }
    //delete
    public ValidatableResponse delete(DeleteRequest deleteRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(deleteRequest)
                .delete("/api/v1/courier/{id}", deleteRequest.getId())
                .then();
    }

}
