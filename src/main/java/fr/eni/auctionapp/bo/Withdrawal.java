package fr.eni.auctionapp.bo;

public class Withdrawal {
    protected Article article;
    protected String street;
    protected String zipCode;
    protected String city;

    public Withdrawal() {

    }

    public Withdrawal(Article article, String street, String zipCode, String city) {
        this.article = article;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
