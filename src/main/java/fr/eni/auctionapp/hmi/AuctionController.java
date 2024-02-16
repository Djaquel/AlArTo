package fr.eni.auctionapp.hmi;

import fr.eni.auctionapp.bll.services.ArticleService;
import fr.eni.auctionapp.bll.services.AuctionService;
import fr.eni.auctionapp.bll.services.MemberService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuctionController {

    private AuctionService auctionService;
    private ArticleService articleService;
    private MemberService memberService;

    public AuctionController(AuctionService auctionService, ArticleService articleService,MemberService memberService) {
        this.auctionService = auctionService;
        this.articleService = articleService;
        this.memberService = memberService;
    }

    @PostMapping("/bet")
    public String placeBet(@RequestParam(name = "montant") int price,
                           @RequestParam(name = "id") int articleId, Authentication authentication) {

        if (auctionService.checkPriceHigher(price, articleId) && memberService.getMemberByPseudo(authentication.getName()).get().isEnabled()) {

            if (articleService.doAuction(price, articleId, authentication) && memberService.getMemberByPseudo(authentication.getName()).get().isEnabled()) {
                return "redirect:/showProfile";
            }
            else {
                // TODO
                // ALERTE TROP PAUVRE
            	System.out.println("ALERTE TROP PAUVRE");
                return "redirect:/article/details/" + articleId;
            }

        } else {
            // TODO
            // ALERTE PRIX TROP BAS
        	System.out.println("ALERTE PRIX TROP BAS");
            return "redirect:/article/details/" + articleId;
        }

    }

}
