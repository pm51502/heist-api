package heist.mapper;

import heist.domain.Skill;
import heist.dto.view.SkillDto;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillDto toDto(Skill skill){
        SkillDto skillDto = new SkillDto();

        skillDto.setName(skill.getName());
        skillDto.setLevel(skill.getLevel());

        return skillDto;
    }

}
