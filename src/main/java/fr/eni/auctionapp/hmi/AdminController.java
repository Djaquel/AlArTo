package fr.eni.auctionapp.hmi;

import fr.eni.auctionapp.bll.services.ArticleService;
import fr.eni.auctionapp.bll.services.CategoryService;
import fr.eni.auctionapp.bll.services.MemberService;
import fr.eni.auctionapp.bo.Category;
import fr.eni.auctionapp.bo.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private MemberService memberService;
    private ArticleService articleService;
    private CategoryService categoryService;

    public AdminController(MemberService memberService, ArticleService articleService,CategoryService categoryService) {
        this.memberService = memberService;
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin")
    public String vueAdmin(Model model) {
        List<Member> members = memberService.getAllMembers();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("members", members);
        model.addAttribute("categories",categories);
        return "pages/admin_page";
    }

    @PostMapping("/admin")
    public String actionAdmin(@RequestParam(name = "action") String action,
                              @RequestParam(name = "memberId") int memberId) {

        switch (action) {
            case "suppr": {
                memberService.deleteMember(memberId);
                break;
            }
            case "deact": {
                memberService.deactivateMember(memberId);
                break;
            }
            case "react": {
                memberService.reactivateMember(memberId);
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + action);
        }

        return "redirect:/admin";
    }

    @GetMapping("/admin/updateAuctionStatus")
    public String updateAuctionStatus(){
        articleService.updateAuctionStatus();
        return "redirect:/admin";
    }
    
    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam(name = "categoryId") int catId) {
    	categoryService.deleteCategoryById(catId);
    	return "redirect:/admin";
    }
    @PostMapping("/addCategory")
    public String newCategory(@RequestParam(name = "categoryLabel")String catName) {
    	categoryService.createCategory(catName);
    	return "redirect:/admin";
    }
}
