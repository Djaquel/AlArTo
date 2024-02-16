package fr.eni.auctionapp.bo;

import java.sql.Date;

public class Article {
    protected int id;
    protected String name;
    protected String description;
    protected Date auctionStartDate;
    protected Date auctionEndDate;
    protected int initialPrice;
    protected int sellPrice;
    protected AuctionState state;
    protected Member seller;
    protected Member buyer;
    protected Category category;

    public Article() {

    }

    public Article(int id, String name, String description, Date auctionStartDate, Date auctionEndDate, int initialPrice, int sellPrice, AuctionState state, Member seller, Member buyer, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.initialPrice = initialPrice;
        this.sellPrice = sellPrice;
        this.state = state;
        this.seller = seller;
        this.buyer = buyer;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAuctionStartDate() {
        return auctionStartDate;
    }

    public void setAuctionStartDate(Date auctionStartDate) {
        this.auctionStartDate = auctionStartDate;
    }

    public Date getAuctionEndDate() {
        return auctionEndDate;
    }

    public void setAuctionEndDate(Date auctionEndDate) {
        this.auctionEndDate = auctionEndDate;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    public Member getSeller() {
        return seller;
    }

    public void setSeller(Member seller) {
        this.seller = seller;
    }

    public Member getBuyer() {
        return buyer;
    }

    public void setBuyer(Member buyer) {
        this.buyer = buyer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article [id=" + id + ", name=" + name + ", description=" + description + ", auctionStartDate="
                + auctionStartDate + ", auctionEndDate=" + auctionEndDate + ", initialPrice=" + initialPrice
                + ", sellPrice=" + sellPrice + ", state=" + state + ", seller=" + seller + ", buyer=" + buyer
                + ", category=" + category + "]";
    }

}
