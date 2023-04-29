package com.example.nextstop.CardsRecycleView;

import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.widget.Space;

public class Cards {

    private String cardLink;
    private Spanned cardDesc;
    private boolean expandable;
    int markerId, numberId;

    public Cards(Spanned cardDesc, String cardLink, int markerId, int numberId) {
        this.cardDesc = cardDesc;
        this.cardLink = cardLink;
        this.expandable = false;
        this.markerId = markerId;
        this.numberId = numberId;
    }

    public Spanned getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(Spanned cardDesc) {
        this.cardDesc = cardDesc;
    }

    public String getCardLink() {
        return cardLink;
    }

    public void setCardLink(String cardLink) {
        this.cardLink = cardLink;
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
                "cardDesc='" + cardDesc + '\'' +
                ", cardLink='" + cardLink + '\'' +
                '}';
    }
}
