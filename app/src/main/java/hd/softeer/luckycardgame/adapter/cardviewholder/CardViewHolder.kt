package hd.softeer.luckycardgame.adapter.cardviewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.state.CardKind

abstract class CardViewHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: Card, position: Int, cardKind: CardKind)
}
