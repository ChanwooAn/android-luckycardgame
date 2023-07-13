package hd.softeer.luckycardgame.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hd.softeer.luckycardgame.adapter.cardviewholder.CardBackViewHolder
import hd.softeer.luckycardgame.adapter.cardviewholder.CardFrontViewHolder
import hd.softeer.luckycardgame.adapter.cardviewholder.CardViewHolder
import hd.softeer.luckycardgame.databinding.RvItemCardBackBinding
import hd.softeer.luckycardgame.databinding.RvItemCardFrontBinding
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.card.CardState
import hd.softeer.luckycardgame.model.state.CardKind

class SharedCardsAdapter(
    private val cardList: List<Card>,
    private val cardWidth: Int,
    private val cardHeight: Int,
    private val onCardClickedInfoUpdateCallback: (position: Int, cardKind: CardKind) -> Unit
) : RecyclerView.Adapter<CardViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    private val onCardClicked = { position: Int, cardKind: CardKind ->
        onCardClickedInfoUpdateCallback(position, cardKind)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = when (viewType) {
            CARD_STATE_FRONT -> {
                RvItemCardFrontBinding.inflate(inflater, parent, false)
            }

            else -> {
                RvItemCardBackBinding.inflate(inflater, parent, false)
            }
        }

        val layoutParams = binding.root.layoutParams
        layoutParams.width = cardWidth
        layoutParams.height = cardHeight


        binding.root.layoutParams = layoutParams
        return if (viewType == CARD_STATE_FRONT) CardFrontViewHolder(binding as RvItemCardFrontBinding) else CardBackViewHolder(
            binding as RvItemCardBackBinding, onCardClicked
        )
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position], position, CardKind.SharedCard)
    }

    override fun getItemViewType(position: Int): Int {
        return if (cardList[position].state == CardState.CARD_OPEN) {
            CARD_STATE_FRONT
        } else {
            CARD_STATE_BACK
        }
    }


    companion object {
        private const val CARD_STATE_FRONT = 100
        private const val CARD_STATE_BACK = 200
    }

}
