package fr.eni.auctionapp.bo;

import java.sql.Date;

public class Auction {
    protected int id;
    protected Date date;
    protected int amount;
    protected Member member;
    protected Article article;

    public Auction() {

    }

    public Auction(int id, Date date, int amount, Member member, Article article) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.member = member;
        this.article = article;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
