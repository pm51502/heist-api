package heist.rest;

import heist.domain.Heist;
import heist.domain.HeistSkill;
import heist.dto.HeistMemberDto;
import heist.dto.view.ConfirmedDto;
import heist.dto.view.EligibleDto;
import heist.dto.HeistSkillDto;
import heist.dto.view.HeistStatusDto;
import heist.dto.view.OutcomeDto;
import heist.service.HeistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/heist")
public class HeistController {

    @Autowired
    private HeistService heistService;

    @PostMapping("")
    public ResponseEntity<?> addHeist(@RequestBody Heist heist){
        Heist createdHeist = heistService.addHeist(heist);

        return ResponseEntity.created(URI.create("/heist/" + createdHeist.getHeistId())).build();
    }

    @PatchMapping("/{id}/skills")
    public ResponseEntity<?> updateSkills(@PathVariable("id") Long heistId, @RequestBody HeistSkillDto heistSkillDto){
        heistService.updateSkills(heistId, heistSkillDto);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heistId + "/skills").build();
    }

    @GetMapping("/{id}/eligible_members")
    public ResponseEntity<?> getEligible(@PathVariable("id") Long heistId){
        EligibleDto eligibleDto = heistService.getEligible(heistId);

        return ResponseEntity.ok(eligibleDto);
    }

    @PutMapping("/{id}/members")
    public ResponseEntity<?> confirmMembers(@PathVariable("id") Long heistId, @RequestBody HeistMemberDto heistMemberDto){
        heistService.confirmMembers(heistId, heistMemberDto);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heistId + "/members").build();
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> startHeist(@PathVariable("id") Long heistId){
        heistService.startHeist(heistId);

        return ResponseEntity.ok().header("Location", "/heist/" + heistId + "/status").build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHeist(@PathVariable("id") Long heistId){
        Heist heist = heistService.getHeist(heistId);

        return ResponseEntity.ok(heist);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getConfirmedMembers(@PathVariable("id") Long heistId){
        ConfirmedDto confirmedDto = heistService.getConfirmedMembers(heistId);

        return ResponseEntity.ok(confirmedDto);
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<?> getRequiredSkills(@PathVariable("id") Long heistId){
        List<HeistSkill> skills = heistService.getRequiredSkills(heistId);

        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable("id") Long heistId){
        HeistStatusDto heistStatusDto = heistService.getStatus(heistId);

        return ResponseEntity.ok(heistStatusDto);
    }

    @GetMapping("/{id}/outcome")
    public ResponseEntity<?> getOutcome(@PathVariable("id") Long heistId){
        OutcomeDto outcomeDto = heistService.getOutcome(heistId);

        return ResponseEntity.ok(outcomeDto);
    }

}