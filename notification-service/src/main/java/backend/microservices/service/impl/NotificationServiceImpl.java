package backend.microservices.service.impl;

import backend.microservices.account.event.AccountCreatedRequest;
import backend.microservices.exception.SendMessageException;
import backend.microservices.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;

    @Override
    public void successfullyCreatedAccountMessage(AccountCreatedRequest request) {
        log.info("Got Message from order-placed topic {}", request);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("spring-bank@email.com");
            messageHelper.setTo(request.getEmail().toString());
            messageHelper.setSubject(String.format("Your Account successfully created!", request.getAccountNumber()));
            messageHelper.setText(String.format("""
                            Hi %s,%s
 
                            Your Account successfully created!.
                             
                            Notification Service
                            """,
                    request.getEmail(),
                    request.getAccountNumber()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Account Notification email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SendMessageException("Exception occurred when sending mail to springshop@email.com");
        }
    }
}