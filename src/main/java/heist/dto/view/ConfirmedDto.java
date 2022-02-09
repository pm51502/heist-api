package heist.dto.view;

import java.util.List;

public class ConfirmedDto {

    private List<MemberDto> members;

    public ConfirmedDto() {}

    public ConfirmedDto(List<MemberDto> members) {
        this.members = members;
    }

    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }
}
