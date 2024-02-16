package fr.eni.auctionapp.hmi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
@RequestMapping("/lang")
public class LocaleController {

    @Autowired
    LocaleResolver localeResolver;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @GetMapping("/fr")
    public String switchToFr() {
        localeResolver.setLocale(request, response, Locale.FRENCH);
        return "redirect:/home" ;
    }

    @GetMapping("/en")
    public String switchToEn() {
        localeResolver.setLocale(request, response, Locale.ENGLISH);
        return "redirect:/home";
    }
}
