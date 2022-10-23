package pojos;

public class GetOrderRequest {
    private Integer courierId;
    private String[] nearestStation;
    private int limit;
    private int page;
    private Integer track;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public String[] getNearestStation() {
        return nearestStation;
    }

    public void setNearestStation(String[] nearestStation) {
        this.nearestStation = nearestStation;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
