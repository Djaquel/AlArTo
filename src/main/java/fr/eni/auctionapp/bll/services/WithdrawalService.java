package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Withdrawal;

import java.util.List;
import java.util.Optional;

public interface WithdrawalService {
    List<Withdrawal> getAllWithdrawals();

    Optional<Withdrawal> getWithdrawalById();

    void insert(Withdrawal withdrawal);

    void update(Withdrawal withdrawal);
}
