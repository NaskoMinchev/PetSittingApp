package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.ContactFormDTO;
import bg.project.petsittingapp.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String petSittingEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender, @Value("${mail.petSitting}") String petSittingEmail) {
        this.javaMailSender = javaMailSender;
        this.petSittingEmail = petSittingEmail;
    }

    @Override
    public void sendIncomingEmails(ContactFormDTO contactFormDTO) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(petSittingEmail);
            mimeMessageHelper.setFrom(contactFormDTO.getEmail());
            mimeMessageHelper.setReplyTo(contactFormDTO.getEmail());
            mimeMessageHelper.setSubject(contactFormDTO.getSubject());
            mimeMessageHelper.setText(contactFormDTO.getMessage());

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
