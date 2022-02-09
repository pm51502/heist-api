package heist.dto.view;

public class HeistStatusDto {

    private String status;

    public HeistStatusDto() {}

    public HeistStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
