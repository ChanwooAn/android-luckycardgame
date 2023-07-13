package hd.softeer.luckycardgame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hd.softeer.luckycardgame.adapter.cardviewholder.CardFrontViewHolder
import hd.softeer.luckycardgame.adapter.cardviewholder.CardViewHolder
import hd.softeer.luckycardgame.databinding.RvItemCardFrontBinding
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.state.CardKind

class ResultCardsAdapter(
    private val cardList: List<Card>,
    private val cardWidth: Int,
    private val cardHeight: Int,
) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RvItemCardFrontBinding.inflate(inflater, parent, false)

        val layoutParams = binding.root.layoutParams
        layoutParams.width = cardWidth
        layoutParams.height = cardHeight


        binding.root.layoutParams = layoutParams
        return CardFrontViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position], position, CardKind.PlayerCard)
    }

}




