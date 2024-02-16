package fr.eni.auctionapp.bll.services;

import fr.eni.auctionapp.bo.Withdrawal;
import fr.eni.auctionapp.dal.WithdrawalDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    WithdrawalDAO withdrawalDAO;

    public WithdrawalServiceImpl(WithdrawalDAO withdrawalDAO) {
        this.withdrawalDAO = withdrawalDAO;
    }


    @Override
    public List<Withdrawal> getAllWithdrawals() {
        return null;
    }

    @Override
    public Optional<Withdrawal> getWithdrawalById() {
        return null;
    }


    @Override
    public void insert(Withdrawal withdrawal) {
        this.withdrawalDAO.insert(withdrawal);

    }


    @Override
    public void update(Withdrawal withdrawal) {
        this.withdrawalDAO.update(withdrawal);

    }
}
