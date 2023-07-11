package hd.softeer.luckycardgame

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import hd.softeer.luckycardgame.databinding.RvItemCardBackBinding
import hd.softeer.luckycardgame.databinding.RvItemCardFrontBinding
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.CardState

class CardSectionAdapter(
    private val userId: Int,
    private val cardList: List<Card>,
    private val cardWidth: Int,
    private val cardHeight: Int,
    private val onCardClickedInfoUpdateCallback: (position: Int, userId: Int) -> Unit,
    private val isTurnCountLeft: (userId: Int) -> Boolean
) :
    RecyclerView.Adapter<CardViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    private val onCardClicked = { position: Int ->
        if (isCardOpenAvailable(position)) {
            onCardClickedInfoUpdateCallback(position, userId)
            this.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding =
            when (viewType) {
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
        holder.bind(cardList[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (cardList[position].state == CardState.CARD_OPEN) {
            CARD_STATE_FRONT
        } else {
            CARD_STATE_BACK
        }
    }

    private fun isCardOpenAvailable(position: Int): Boolean {
        val positionAvailability =
            (position - 1 >= 0 && cardList[position - 1].state == CardState.CARD_OPEN) ||
                    (position + 1 < cardList.size && cardList[position + 1].state == CardState.CARD_OPEN) ||
                    (position == 0 || position == cardList.size - 1)

        return userId < 100 && isTurnCountLeft(userId) && positionAvailability
        //user가 뒤집을 수 있는 횟수가 남았는지 && 가장 앞이나 뒷쪽인지 && user영역의 카드인지, shared영역인지
    }

    companion object {
        private const val CARD_STATE_FRONT = 100
        private const val CARD_STATE_BACK = 200
    }

}

abstract class CardViewHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: Card, position: Int)
}

class CardBackViewHolder(
    binding: RvItemCardBackBinding,
    private val onCardClicked: (position: Int) -> Unit
) :
    CardViewHolder(binding) {
    override fun bind(item: Card, position: Int) {
        itemView.setOnClickListener {
            onCardClicked(position)
        }
    }

}

class CardFrontViewHolder(private val binding: RvItemCardFrontBinding) :
    CardViewHolder(binding) {
    override fun bind(item: Card, position: Int) {
        binding.apply {
            tvAnimalUnicode.text = item.type.emoji
            tvCardNumberTopLeft.text = item.number.num.toString()
            tvCardNumberBottomRight.text = item.number.num.toString()
        }
    }
}

// RecyclerView ItemDecorator 클래스
class PlayerCardItemDecorator(private val cardSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        // 카드가 8장일 때 중간에 겹치도록 설정
        val moveSize = cardSize * 0.25

        if (position < itemCount - 1) {
            if (itemCount == 7) {
                outRect.right -= moveSize.toInt()
            } else if (itemCount == 8) {
                outRect.right -= moveSize.toInt() + 4
            }
        }

        if (itemCount < 7) {
            outRect.left += 5
        }
    }
}

class SharedCardItemDecorator(private val spanCount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val space = when (spanCount) {
            5 -> {
                40
            }

            4 -> {
                56
            }

            else -> {
                12
            }
        }
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = space - column * space / spanCount
        outRect.right = (column + 1) * space / spanCount
        if (position >= spanCount) {
            outRect.top = space
        }
    }
}
