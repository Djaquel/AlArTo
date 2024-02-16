package fr.eni.auctionapp.bo;

public class Member {
    protected int id;
    protected String pseudo;
    protected String lastname;
    protected String firstname;
    protected String email;
    protected String phone;
    protected String street;
    protected String zipCode;
    protected String city;
    protected String password;
    protected int credits;
    protected boolean isAdmin;
    protected boolean isEnabled;

    public Member() {

    }

    public Member(int id, String pseudo, String lastname, String firstname, String email, String phone, String street, String zipCode, String city, String password, int credits, boolean isAdmin, boolean isEnabled) {
        this.id = id;
        this.pseudo = pseudo;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.password = password;
        this.credits = credits;
        this.isAdmin = isAdmin;
        this.isEnabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return "Member [id=" + id + ", pseudo=" + pseudo + ", lastname=" + lastname + ", firstname=" + firstname
                + ", email=" + email + ", phone=" + phone + ", street=" + street + ", zipCode=" + zipCode + ", city="
                + city + ", password=" + password + ", credits=" + credits + ", isAdmin=" + isAdmin + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Member)){
            return false;
        }
        return ((Member) o).getId() == this.getId();
    }
}
