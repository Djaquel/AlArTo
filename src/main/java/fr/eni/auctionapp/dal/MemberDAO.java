package fr.eni.auctionapp.dal;

import fr.eni.auctionapp.bo.Member;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface MemberDAO extends DAO<Member> {

    Optional<Member> findByPseudo(String pseudo);

    Optional<Member> findByEmail(String email);

    int insert(Member user);

    void removeById(int memberId);

    void update(Member member, Authentication authentication);

    void updatePasswordByMemberId(int memberId, String encodedPassword);

    void updateCredits(Member member, int credits);

    void supprMemberLogout(Authentication authentication);

    void deactivateMemberDAO(int memberId);

    void reactivateMemberDAO(int memberId);

    void nullableSellerId(int memberId);

    void nullableBuyerId(int memberId);
}
