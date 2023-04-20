package com.example.nextstop;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    List<Cards> cardsList;
    RecyclerView recyclerView;

    public CardsAdapter(List<Cards> cardsList, RecyclerView recyclerView) {
        this.cardsList = cardsList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new CardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {

        Cards cards = cardsList.get(position);
        holder.cardTitle.setText(cards.getCardTitle());
        holder.cardDesc.setText(cards.getCardDesc());
        holder.markerImage.setBackgroundResource(cards.getMarkerId());
        holder.numberImage.setBackgroundResource(cards.getNumberId());

        boolean isExpandable = cardsList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {

        TextView cardTitle, cardDesc;
        ImageView markerImage, numberImage;
        LinearLayout cardLayout, expandableLayout;
        ConstraintLayout cardTitleLayout;

        public CardsViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTitle = itemView.findViewById(R.id.card_title);
            cardDesc = itemView.findViewById(R.id.card_desc);
            markerImage = itemView.findViewById(R.id.markerView);
            numberImage = itemView.findViewById(R.id.numberView);

            cardLayout = itemView.findViewById(R.id.card_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            cardTitleLayout = itemView.findViewById(R.id.title_layout);

            cardTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cards cards = cardsList.get(getAdapterPosition());
                    int currentPosition = getAdapterPosition();
                    cards.setExpandable(!cards.isExpandable());
                    notifyItemChanged(currentPosition);
                    if (cards.isExpandable()) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            layoutManager.scrollToPositionWithOffset(currentPosition, 0);
                        }
                    }
                }
            });
        }
    }
}
