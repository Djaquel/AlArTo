package fr.eni.auctionapp.hmi;

import fr.eni.auctionapp.bll.services.MemberService;
import fr.eni.auctionapp.bll.services.SecurityService;
import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.bo.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    SecurityService securityService;
    @Autowired
    MemberService memberService;

    @RequestMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordGet() {
        return "pages/reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordPost(@RequestParam(name = "email") String email, Locale locale) {
        Optional<Member> member = memberService.getMemberByEmail(email);
        member.ifPresent(value -> securityService.sendResetPasswordToken(locale, value));
        return "redirect:/login";
    }

    @GetMapping("/changePassword")
    public String changePasswordGet(Model model, @RequestParam("token") String token) {
        Optional<PasswordResetToken> resetTokenOpt = securityService.getResetTokenByTokenString(token);
        boolean isValid = resetTokenOpt.isPresent();
        model.addAttribute("isValid", isValid);
        if (isValid) {
            model.addAttribute("member", resetTokenOpt.get().getMember());
            model.addAttribute("token", token);
        }
        return "pages/new_password";
    }

    @PostMapping("/changePassword")
    public String changePasswordPost(@RequestParam("password") String password, @RequestParam("token") String token,
                                     @RequestParam("memberId") int memberId, Model model) {

        if (securityService.isValidToken(token, memberId)) {
            memberService.updatePassword(password, memberId);
            return "redirect:/login";
        } else {
            model.addAttribute("isValid", false);
            return "pages/new_password";
        }
    }
}
