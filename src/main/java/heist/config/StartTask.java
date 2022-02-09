package heist.config;

import heist.dao.HeistRepository;
import heist.domain.Heist;
import heist.domain.Member;
import heist.domain.enums.HeistStatus;
import heist.service.EmailService;

public class StartTask implements Runnable {

    private final HeistRepository heistRepository;

    private final EmailService emailService;

    private final Long heistId;

    public StartTask(Long heistId, HeistRepository heistRepository, EmailService emailService) {
        this.heistId = heistId;
        this.heistRepository = heistRepository;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        Heist heist = heistRepository.findByHeistId(heistId);

        for (Member member : heist.getConfirmedMembers()) {
            emailService.SendSimpleMessage(member.getEmail(),
                    "Heist start",
                    "Heist '" + heist.getName() + "' has started\nHeist API team");
        }

        heist.setStatus(HeistStatus.IN_PROGRESS);
        heistRepository.save(heist);
    }
}
