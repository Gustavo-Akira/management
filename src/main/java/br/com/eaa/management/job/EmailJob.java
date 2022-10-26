package br.com.eaa.management.job;

import br.com.eaa.management.dto.email.EmailDTO;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.repository.ReservationRepository;
import br.com.eaa.management.repository.UserRepository;
import br.com.eaa.management.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailJob {
    static final Logger LOGGER = LoggerFactory.getLogger(EmailJob.class);

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService service;

    @Scheduled(cron = "0 37 11 * * *")
    public void  sendEmails(){
        List<Reservation> reservations = repository.findAllByStartTime(LocalDateTime.now().plusDays(1L).toLocalDate().atStartOfDay(), LocalDateTime.now().plusDays(2L).toLocalDate().atStartOfDay());
        reservations.forEach(x->{
            EmailDTO dto = new EmailDTO();
            dto.setRecipient(x.getLocator().getEmail());
            dto.setMsgBody("Por favor clique no link abaixo http://localhost:8083/api/v1/reservation/confirmation/"+x.getId());
            dto.setSubject("http://localhost:8083/api/v1/reservation/confirmation/"+x.getId());
            try {
                service.sendSimpleMail(dto);
            } catch (Exception e) {
                LOGGER.error("Error while sending email");
            }
        });
    }

    @Scheduled(cron = "0 00 7 * * *")
    public void deleteAndAlertAll(){
        List<Reservation> reservations = repository.findAllByStartTime(LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
        Map<String, String> locationAndData = new HashMap<>();
        reservations.forEach(x->{
            if(!x.getIsConfirmed()) {
                locationAndData.put(x.getLocation().getName(),x.getStartTime() + " até "+x.getEndTime());
                repository.deleteById(x.getId());
            }
        });
        userRepository.findAll().forEach(x->{
            EmailDTO dto = new EmailDTO();
            dto.setRecipient(x.getEmail());
            dto.setMsgBody(locationAndData.entrySet().stream().map((data)-> data.getKey() + " " + data.getValue()).collect(Collectors.joining("\n")));
            dto.setSubject("Ambientes disponíveis no dia de hoje por não confirmação ");
            try {
                service.sendSimpleMail(dto);
            } catch (Exception e) {
                LOGGER.error("Error while sending email");
            }
        });
    }
}
