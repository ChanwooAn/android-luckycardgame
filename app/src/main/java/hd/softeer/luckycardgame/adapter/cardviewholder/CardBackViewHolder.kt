package hd.softeer.luckycardgame.adapter.cardviewholder

import hd.softeer.luckycardgame.databinding.RvItemCardBackBinding
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.state.CardKind

class CardBackViewHolder(
    binding: RvItemCardBackBinding,
    private val onCardClicked: (position: Int, cardKind: CardKind) -> Unit
) : CardViewHolder(binding) {
    override fun bind(item: Card, position: Int, cardKind: CardKind) {
        itemView.setOnClickListener {
            onCardClicked(position, cardKind)
        }
    }

}
