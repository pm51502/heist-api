package heist.mapper;

import heist.domain.Skill;
import heist.dto.view.SkillsDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SkillsMapper {
    public SkillsDto toDto(List<Skill> skills, String mainSkill){
        return new SkillsDto(skills, mainSkill);
    }
}
