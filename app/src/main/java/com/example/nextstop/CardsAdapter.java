package com.example.nextstop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    List<Cards> cardsList;

    public CardsAdapter(List<Cards> cardsList) {
        this.cardsList = cardsList;
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

        boolean isExpandable = cardsList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {

        TextView cardTitle, cardDesc;
        View cardLine;
        LinearLayout cardLayout, expandableLayout;

        public CardsViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTitle = itemView.findViewById(R.id.card_title);
            cardDesc = itemView.findViewById(R.id.card_desc);

            cardLayout = itemView.findViewById(R.id.card_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Cards cards = cardsList.get(getAdapterPosition());
                    cards.setExpandable(!cards.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
