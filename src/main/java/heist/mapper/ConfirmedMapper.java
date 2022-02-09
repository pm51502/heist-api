package heist.mapper;

import heist.domain.Member;
import heist.dto.view.ConfirmedDto;
import heist.dto.view.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;

@Component
public class ConfirmedMapper {

    @Autowired
    private MemberMapper memberMapper;

    public ConfirmedDto toDto(List<Member> members){
        ConfirmedDto confirmedDto = new ConfirmedDto();
        List<MemberDto> memberDtos = new LinkedList<>();

        for(Member member : members)
            memberDtos.add(memberMapper.toDto(member));

        confirmedDto.setMembers(memberDtos);
        return confirmedDto;
    }

}