package com.example.nextstop;

public class Cards {

    private String cardTitle, cardDesc;
    private boolean expandable;

    public Cards(String cardTitle, String cardDesc) {
        this.cardTitle = cardTitle;
        this.cardDesc = cardDesc;
        this.expandable = false;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void toggleExpandable() {
        this.expandable = !this.expandable;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "cardTitle='" + cardTitle + '\'' +
                ", cardDesc='" + cardDesc + '\'' +
                '}';
    }
}
