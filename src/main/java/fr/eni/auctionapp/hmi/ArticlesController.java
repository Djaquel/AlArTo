package fr.eni.auctionapp.hmi;

import fr.eni.auctionapp.bll.services.*;
import fr.eni.auctionapp.bo.*;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class ArticlesController {


    private MemberService memberService;
    private CategoryService categoryService;
    private ArticleService articleService;
    private WithdrawalService withdrawalService;
    private StorageService storageService;
    private AuctionService auctionService;


    public ArticlesController(MemberService memberService, CategoryService categoryService, ArticleService articleService, WithdrawalService withdrawalService, StorageService storageService,AuctionService auctionService) {
        this.memberService = memberService;
        this.categoryService = categoryService;
        this.articleService = articleService;
        this.withdrawalService = withdrawalService;
        this.storageService = storageService;
        this.auctionService = auctionService;
        storageService.init();
    }

    @GetMapping("/sellingPage")
    public String sellingPage(Model model, Authentication authentication) {
    	if(memberService.getMemberByPseudo(authentication.getName()).get().isEnabled()) {
        //getCategorie
        List<Category> categories = categoryService.getAllCategories();
        //getMember
        Optional<Member> member = memberService.getMemberByPseudo(authentication.getName());

        model.addAttribute("categories", categories);
        model.addAttribute("member", member.get());
        return "pages/sell_article";
        }else {
        	return "redirect:/home";
        }
    }

    @PostMapping("/sellItem")
    public String iniSelling(@RequestParam(name = "nameArticle") String nameArticle,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "idCategory") int idCategory,
                             @RequestParam(name = "price") int price,
                             @RequestParam(name = "startingDate") String startingDate,
                             @RequestParam(name = "endingDate") String endingDate,
                             @RequestParam(name = "withdrawalStreet") String withdrawalStreet,
                             @RequestParam(name = "withdrawalZipCode") String withdrawalZipCode,
                             @RequestParam(name = "withdrawalCity") String withdrawalCity,
                             @RequestParam(name = "picture") MultipartFile picture,
                             Authentication authentication
    ) {

        Article article = new Article();
        article.setName(nameArticle);
        article.setDescription(description);
        article.setCategory(categoryService.getCategoryById(idCategory).get());
        article.setInitialPrice(price);
        article.setSellPrice(price);
        article.setAuctionStartDate(Date.valueOf(startingDate));
        article.setAuctionEndDate(Date.valueOf(endingDate));
        article.setSeller(memberService.getMemberFromAuthentication(authentication));
        article.setBuyer(article.getSeller());
        article.setState(AuctionState.UNSTARTED);
        this.articleService.insert(article);
        storageService.save(picture, String.valueOf(article.getId()), ResourceType.ARTICLE_IMAGE);

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setArticle(article);
        withdrawal.setStreet(withdrawalStreet);
        withdrawal.setZipCode(withdrawalZipCode);
        withdrawal.setCity(withdrawalCity);

        this.withdrawalService.insert(withdrawal);

        return "redirect:/showProfile";
    }


    @GetMapping("/modifSell")
    public String vueModifSell(@RequestParam(name = "idArticle") int id, Model model, Authentication authentication) {
        model.addAttribute("article", articleService.getArticleById(id).get());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("member", memberService.getMemberFromAuthentication(authentication));
        return "pages/edit_article";
    }

    @PostMapping("/modifSell")
    public String modifSell(@RequestParam(name = "idArticle") int id,
                            @RequestParam(name = "nameArticle") String nameArticle,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "idCategory") int idCategory,
                            @RequestParam(name = "price") int price,
                            @RequestParam(name = "startingDate") String startingDate,
                            @RequestParam(name = "endingDate") String endingDate,
                            @RequestParam(name = "withdrawalStreet") String withdrawalStreet,
                            @RequestParam(name = "withdrawalZipCode") String withdrawalZipCode,
                            @RequestParam(name = "withdrawalCity") String withdrawalCity,
                            @RequestParam(name = "picture") MultipartFile picture,
                            Authentication authentication) {

        Date auctionStart = Date.valueOf(startingDate);
        Date auctionEnd = Date.valueOf(endingDate);

        if(auctionEnd.before(auctionStart)){
            throw new IllegalArgumentException("Illegal end date : before start");
        }

        Article article = new Article();
        article.setId(id);
        article.setName(nameArticle);
        article.setDescription(description);
        article.setCategory(categoryService.getCategoryById(idCategory).get());
        article.setInitialPrice(price);
        article.setSellPrice(price);
        article.setAuctionStartDate(auctionStart);
        article.setAuctionEndDate(auctionEnd);
        article.setSeller(memberService.getMemberFromAuthentication(authentication));
        article.setBuyer(article.getSeller());
        article.setState(AuctionState.UNSTARTED);
        this.articleService.update(article);
        if (!picture.isEmpty()) {
            storageService.save(picture, String.valueOf(article.getId()), ResourceType.ARTICLE_IMAGE);
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setArticle(article);
        withdrawal.setStreet(withdrawalStreet);
        withdrawal.setZipCode(withdrawalZipCode);
        withdrawal.setCity(withdrawalCity);

        this.withdrawalService.update(withdrawal);

        return "redirect:/showProfile";
    }

    @PostMapping("/deleteSale")
    public String deleteSale(@RequestParam(name = "idArticle") int id) {
        articleService.deleteArticle(id);
        return "redirect:/showProfile";
    }


    @GetMapping(path = {"/article/details/{id}"})
    public String articleDetails(
            @PathVariable("id") int articleId,
            Model model, Authentication authentication) {

        Article article = null;
        List<Auction> auctions;
        Optional<Article> optArt = articleService.getArticleById(articleId);
        if (optArt.isEmpty()) {
            return "redirect:/home";
        } else {
            article = optArt.get();
            auctions = auctionService.getAuctionsByArticleId(articleId);
        }
       
        model.addAttribute("article", article);
        model.addAttribute("id", articleId);
        model.addAttribute("articleAuctions",auctions);
        model.addAttribute("curMember",memberService.getMemberByPseudo(authentication.getName()).get().getId());
        return "pages/article_details";
    }

    @GetMapping({"/resources/article/{id}"})
    @ResponseBody
    public Resource getArticlePicture(@PathVariable String id) {
        try {
            return storageService.load(id, ResourceType.ARTICLE_IMAGE);
        } catch (Exception ex) {
            return null;
        }
    }

}
