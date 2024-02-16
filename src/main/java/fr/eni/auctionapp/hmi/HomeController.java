package fr.eni.auctionapp.hmi;


import fr.eni.auctionapp.bll.services.ArticleService;
import fr.eni.auctionapp.bo.Article;
import fr.eni.auctionapp.bo.Pageable;
import fr.eni.auctionapp.bo.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;

    private final int ITEM_QUANTITY = 6;

    @GetMapping(path = {"/", "/home", "/auctions"})
    public String home(Model model, Authentication authentication) {
        Search search = new Search();
        Pageable<Article> articles = articleService.getFilteredArticles(search, authentication, 0, ITEM_QUANTITY);
        model.addAttribute("articles", articles);
        model.addAttribute("search", search);

        return "pages/home";
    }

    @PostMapping(path = {"/article/search"})
    public String search(@ModelAttribute Search search, @RequestParam(name = "pageSelect") int page, Model model, Authentication authentication) {
        Pageable<Article> articles = articleService.getFilteredArticles(search, authentication, page, ITEM_QUANTITY);
        model.addAttribute("articles", articles);
        model.addAttribute("search", search);

        return "pages/home";
    }
}
