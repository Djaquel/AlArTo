package fr.eni.auctionapp.security;

import fr.eni.auctionapp.bll.services.MemberService;
import fr.eni.auctionapp.bo.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MemberUserDetails implements UserDetails {

    private Member member;
    private MemberService memberService;

    public MemberUserDetails(Member member, MemberService memberService) {
        this.member = member;
        this.memberService = memberService;
    }

    public Member getMember() {
        return memberService.getMemberById(member.getId()).orElse(null);
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (member.isAdmin()) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getPseudo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
