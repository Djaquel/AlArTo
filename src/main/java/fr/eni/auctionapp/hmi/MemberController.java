package fr.eni.auctionapp.hmi;


import fr.eni.auctionapp.bll.services.ArticleService;
import fr.eni.auctionapp.bll.services.MemberService;
import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.Member;
import fr.eni.auctionapp.security.MemberUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class MemberController {

    private MemberService memberService;
    private UserDetailsService userDetailsService;
    private ArticleService articleService;
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";


    public MemberController(MemberService memberService, UserDetailsService userDetailsService, ArticleService articleService) {
        this.memberService = memberService;
        this.userDetailsService = userDetailsService;
        this.articleService = articleService;
    }


    @GetMapping("/signup")
    public String goToSingUp(Model model) {
        model.addAttribute("member", new Member());
        return "pages/sign_up";
    }

    @PostMapping("/signup")
    public String singUp(@Valid @ModelAttribute Member member, BindingResult bindingResult, HttpServletRequest request, Locale locale) {
        if (bindingResult.hasErrors()) {
            return "signup";
        } else {
            member.setCredits(0);
            member.setAdmin(false);
            member.setEnabled(true);
        }
        this.memberService.createMember(locale, member);

        MemberUserDetails userDetails = (MemberUserDetails) userDetailsService.loadUserByUsername(member.getPseudo());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, member.getPassword(), userDetails.getAuthorities());
        if (auth.isAuthenticated()) {
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        }

        return "redirect:/home";
    }

    @GetMapping("/editProfile")
    public String editProfile(Model model) {
        Optional<Member> currentMember = memberService.getAuthenticatedMember();
        if (currentMember.isEmpty()) {
            return "/login";
        } else {
            model.addAttribute("member", currentMember.get());
            return "pages/edit_profile";
        }
    }


    @PostMapping("/editProfile")
    public String editProfile(@Valid @ModelAttribute Member member,
                              @RequestParam(name="oldPassword") String oldPassword, @RequestParam(name="newPassword") String newPassword, BindingResult bindingResult, Authentication authentication) {

            if (bindingResult.hasErrors()) {
                return "pages/edit_profile";
            }
            // You can access the Member object directly from the model
            this.memberService.updateUser(member, oldPassword, newPassword, authentication);
            return "redirect:/showProfile";
    }

    @PostMapping("/deleteProfile")
    public String deleteProfile() {
        Member member = memberService.getAuthenticatedMember().get();
        memberService.deleteMember(member.getId());
        return "redirect:/logout";
    }

    @GetMapping("/showProfile")
    public String showProfile(Model model) {
        Optional<Member> currentMember = memberService.getAuthenticatedMember();
        if (currentMember.isEmpty()) {
            return "/login";
        } else {
            Member member = currentMember.get();
            List<Article> Articles = articleService.getAllMyArticles(member.getId());
            model.addAttribute("member", member);
            model.addAttribute("articleList", Articles);
            return "pages/profile";
        }
    }

    @PostMapping("/showProfile")
    public String showProfile(@RequestParam("buttonAction") String buttonAction, Model model, Authentication authentication) {
        Optional<Member> currentMember = memberService.getMemberByPseudo(authentication.getName());
        if (currentMember.isEmpty()) {
            return "/login";
        }
        if ("modif".equals(buttonAction)) {
            model.addAttribute("member", currentMember.get());
            return "pages/edit_profile";
        } else if ("suppr".equals(buttonAction)) {
            memberService.removeMember(authentication);
            return "redirect:/logout";
        }
        return "pages/home";
    }

}
