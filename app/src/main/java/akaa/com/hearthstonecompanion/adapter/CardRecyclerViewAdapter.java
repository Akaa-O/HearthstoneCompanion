package akaa.com.hearthstonecompanion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import akaa.com.hearthstonecompanion.InfoViewListener;
import akaa.com.hearthstonecompanion.R;
import akaa.com.hearthstonecompanion.model.Card;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.MViewHolder> {
    private List<Card> myCards;
    private LayoutInflater mInflater;
    private Context mContext;

    public static class MViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImage;
        public TextView cardName;
        public TextView cardClass;
        public TextView cardType;


        public MViewHolder(View cardView) {
            super(cardView);
            cardImage = cardView.findViewById(R.id.card_image);
            cardName = cardView.findViewById(R.id.card_name);
            cardClass = cardView.findViewById(R.id.card_class);
            cardType = cardView.findViewById(R.id.card_type);
        }
    }

    public CardRecyclerViewAdapter(Context context, List<Card> cards, InfoViewListener infoViewListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        myCards = cards;
        infoViewListener.hideLoadingView();

        if (myCards.size() == 0){
            infoViewListener.showNoCardsView();
        } else {
            infoViewListener.hideNoCardsView();
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_item, parent, false);

        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int position) {
        Card mCard = myCards.get(position);

        if(mCard.getCardImagePath() != null){
            Picasso.with(mContext).load(mCard.getCardImagePath()).into(holder.cardImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    holder.cardImage.setImageResource(R.drawable.placeholder);
                }
            });
        } else {
            holder.cardImage.setImageResource(R.drawable.placeholder);
        }
        holder.cardName.setText(mCard.getCardName());
        holder.cardClass.setText(mCard.getCardClass());
        holder.cardType.setText(mCard.getCardType());

    }

    @Override
    public int getItemCount() {
        return myCards.size();
    }

}
