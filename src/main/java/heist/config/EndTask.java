package heist.config;

import heist.dao.HeistRepository;
import heist.domain.Heist;
import heist.domain.Member;
import heist.domain.enums.HeistStatus;
import heist.service.EmailService;

public class EndTask implements Runnable {

    private final HeistRepository heistRepository;

    private final EmailService emailService;

    private final Long heistId;

    public EndTask(Long heistId, HeistRepository heistRepository, EmailService emailService) {
        this.heistId = heistId;
        this.heistRepository = heistRepository;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        Heist heist = heistRepository.findByHeistId(heistId);

        for (Member member : heist.getConfirmedMembers()) {
            emailService.SendSimpleMessage(member.getEmail(),
                    "Heist finish",
                    "Heist '" + heist.getName() + "' has finished\nHeist API team");
        }

        heist.setStatus(HeistStatus.FINISHED);
        heistRepository.save(heist);
    }
}
