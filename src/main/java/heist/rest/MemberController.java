package heist.rest;

import heist.domain.Member;
import heist.domain.Skill;
import heist.dto.MemberSkillDto;
import heist.dto.view.SkillsDto;
import heist.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("")
    public ResponseEntity<?> addMember(@RequestBody Member member){
        Member createdMember = memberService.addMember(member);

        return ResponseEntity.created(URI.create("/member/" + createdMember.getMemberId())).build();
    }

    @PutMapping("/{id}/skills")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long memberId, @RequestBody MemberSkillDto memberSkillDto){
        memberService.updateMember(memberId, memberSkillDto);

        return ResponseEntity.noContent().header("Content-Location", "/member/" + memberId + "/skills").build();
    }

    @DeleteMapping("/{id}/skills/{skillName}")
    public ResponseEntity<?> deleteSkill(@PathVariable("id") Long memberId, @PathVariable("skillName") String skillName){
        memberService.deleteSkill(memberId, skillName);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable("id") Long memberId){
        Member member = memberService.getMember(memberId);

        return ResponseEntity.ok(member);
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<?> getSkills(@PathVariable("id") Long memberId){
        SkillsDto skillsDto = memberService.getSkills(memberId);

        return ResponseEntity.ok(skillsDto);
    }

    @GetMapping("")
    public List<Member> listMembers(){
        return memberService.listAll();
    }

}