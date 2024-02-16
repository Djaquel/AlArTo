package fr.eni.auctionapp.dal;


import fr.eni.auctionapp.bo.Withdrawal;

public interface WithdrawalDAO extends DAO<Withdrawal> {

    void update(Withdrawal withdrawal);

}
