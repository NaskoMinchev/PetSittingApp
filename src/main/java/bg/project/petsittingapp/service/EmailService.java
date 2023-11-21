package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.ContactFormDTO;

public interface EmailService {
    void sendIncomingEmails(ContactFormDTO contactFormDTO);
}
