package backend.microservices.service.impl;

import backend.microservices.account.kafka.event.AccountCreatedRequest;
import backend.microservices.account.kafka.event.UpdateBalanceRequest;
import backend.microservices.exception.SendMessageException;
import backend.microservices.external.AccountBalance;
import backend.microservices.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final RestTemplate restTemplate;

    @Override
    public void successfullyCreatedAccountMessage(AccountCreatedRequest request) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("spring-bank@gmail.com");
            messageHelper.setTo(request.getEmail());
            messageHelper.setSubject(String.format("Your Account successfully created!", request.getAccountNumber()));
            messageHelper.setText(String.format("""
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

    @Override
    public void successUpdateBalance(UpdateBalanceRequest request) {
        AccountBalance accountBalance = restTemplate.getForObject("http://account-service:8082/api/v1/accounts/balance/" + request.getAccountNumber(),
                AccountBalance.class);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("spring-bank@gmail.com");
            messageHelper.setTo(request.getEmail());
            messageHelper.setSubject(String.format("Пополнение!", request.getAccountNumber()));
            messageHelper.setText(String.format("""
                            Ваш счет успешно пополнен!""" +
                            """
                            Ваш счет:""" + Objects.requireNonNull(accountBalance).getBalance().toString() +
                            """ 
                            Notification Service
                            """,
                    request.getEmail(),
                    request.getAccountNumber()));
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SendMessageException("Exception occurred when sending mail to springshop@gmail.com");
        }
    }
}