package hd.softeer.luckycardgame.adapter.cardviewholder

import hd.softeer.luckycardgame.databinding.RvItemCardFrontBinding
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.state.CardKind

class CardFrontViewHolder(private val binding: RvItemCardFrontBinding) : CardViewHolder(binding) {
    override fun bind(item: Card, position: Int, cardKind: CardKind) {
        binding.apply {
            tvAnimalUnicode.text = item.type.emoji
            tvCardNumberTopLeft.text = item.number.num.toString()
            tvCardNumberBottomRight.text = item.number.num.toString()
        }
    }
}