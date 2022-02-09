package heist.dto;

import java.util.List;

public class HeistMemberDto {

    List<String> members;

    public HeistMemberDto() {}

    public HeistMemberDto(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
