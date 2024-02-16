package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Member;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface MemberService {

    Optional<Member> getMemberById(int memberId);

    void createMember(Locale locale, Member member);

    void updateUser(Member member, String oldPassword, String newPassword, Authentication authentication);

    List<Member> getAllMembers();

    void updatePassword(String password, int memberId);

    Optional<Member> getMemberByPseudo(String pseudo);

    Optional<Member> getMemberByEmail(String email);

    void removeMember(Authentication authentication);

    Optional<Member> getAuthenticatedMember();

    void updateAuthenticatedMember(Member member);

    Member getMemberFromAuthentication(Authentication authentication);

    void deactivateMember(int memberId);

    void deleteMember(int memberId);

    void reactivateMember(int memberId);
}
