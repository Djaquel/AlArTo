package fr.eni.auctionapp.hmi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.auctionapp.bll.services.CreditService;
import fr.eni.auctionapp.bll.services.MemberService;
import fr.eni.auctionapp.bo.Member;

@Controller
public class CreditController {
	
	private MemberService memberService;
	private CreditService creditService;
	
	public CreditController(MemberService memberService,CreditService creditService) {
		this.memberService = memberService;
		this.creditService = creditService;
	}

	@GetMapping("/buyCredit")
	public String showBuyCreditPage() {
		return "pages/buy_credit";
	}
	
	@PostMapping("/buy")
	public String buyCredit(@RequestParam(name = "numberCredit")int numberCredit) {
		Member member = memberService.getAuthenticatedMember().get();
		creditService.addCredits(member,numberCredit);
		return "redirect:/home";
	}
}
