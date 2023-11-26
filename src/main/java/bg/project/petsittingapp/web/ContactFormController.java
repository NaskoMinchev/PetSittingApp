package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.ContactFormDTO;
import bg.project.petsittingapp.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactFormController {
    private final EmailService emailService;

    public ContactFormController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/contact")
    public ModelAndView contact(@ModelAttribute("contactFormDTO") ContactFormDTO contactFormDTO) {
        return new ModelAndView("contact");
    }

    @PostMapping("/contact/send")
    public ModelAndView sendMessage(@ModelAttribute("contactFormDTO")
                                        @Valid ContactFormDTO contactFormDTO,
                                        BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("contactFormDTO", contactFormDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactFormDTO", bindingResult);
            return new ModelAndView("/contact");
        }


        emailService.sendIncomingEmails(contactFormDTO);

        ModelAndView modelAndView = new ModelAndView("/contact");
        modelAndView.addObject("message_send", true);

        return modelAndView;
    }
}
