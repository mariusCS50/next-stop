package com.example.nextstop.CardsRecycleView;

import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.widget.Space;

public class Cards {

    private String cardTitle;
    private Spanned cardDesc;
    private boolean expandable;
    int markerId, numberId;

    public Cards(String cardTitle, Spanned cardDesc, int markerId, int numberId) {
        this.cardTitle = cardTitle;
        this.cardDesc = cardDesc;
        this.expandable = false;
        this.markerId = markerId;
        this.numberId = numberId;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public Spanned getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(Spanned cardDesc) {
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

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public int getNumberId() {
        return numberId;
    }

    public void setNumberId(int numberId) {
        this.numberId = numberId;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "cardTitle='" + cardTitle + '\'' +
                ", cardDesc='" + cardDesc + '\'' +
                '}';
    }
}
