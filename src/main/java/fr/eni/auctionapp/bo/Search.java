package fr.eni.auctionapp.bo;

public class Search {
    int categoryId;
    String searchText;
    boolean ownArticles;
    boolean openedAuctions;
    boolean currentAuctions;
    boolean wonAuctions;
    boolean currentSales;
    boolean unstartedSales;
    boolean finishedSales;
    Member connectedMember;

    public Search() {
        categoryId = 1;
        searchText = "";
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public boolean isOwnArticles() {
        return ownArticles;
    }

    public void setOwnArticles(boolean ownArticles) {
        this.ownArticles = ownArticles;
    }

    public boolean isOpenedAuctions() {
        return openedAuctions;
    }

    public void setOpenedAuctions(boolean openedAuctions) {
        this.openedAuctions = openedAuctions;
    }

    public boolean isCurrentAuctions() {
        return currentAuctions;
    }

    public void setCurrentAuctions(boolean currentAuctions) {
        this.currentAuctions = currentAuctions;
    }

    public boolean isWonAuctions() {
        return wonAuctions;
    }

    public void setWonAuctions(boolean wonAuctions) {
        this.wonAuctions = wonAuctions;
    }

    public boolean isCurrentSales() {
        return currentSales;
    }

    public void setCurrentSales(boolean currentSales) {
        this.currentSales = currentSales;
    }

    public boolean isUnstartedSales() {
        return unstartedSales;
    }

    public void setUnstartedSales(boolean unstartedSales) {
        this.unstartedSales = unstartedSales;
    }

    public boolean isFinishedSales() {
        return finishedSales;
    }

    public void setFinishedSales(boolean finishedSales) {
        this.finishedSales = finishedSales;
    }

    public Member getConnectedMember() {
        return connectedMember;
    }

    public void setConnectedMember(Member connectedMember) {
        this.connectedMember = connectedMember;
    }

    public boolean hasFilters() {
        return ownArticles ? (currentSales || unstartedSales || finishedSales)
                : (openedAuctions || currentAuctions || wonAuctions);
    }
}
