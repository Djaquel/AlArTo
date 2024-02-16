package fr.eni.auctionapp.security;

import fr.eni.auctionapp.bll.services.MemberService;
import fr.eni.auctionapp.bo.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private MemberService memberService;

    public UserDetailsServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optMember = memberService.getMemberByPseudo(username);
        if (optMember.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return new MemberUserDetails(optMember.get(), memberService);
    }

}


