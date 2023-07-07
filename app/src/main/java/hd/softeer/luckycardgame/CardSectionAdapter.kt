package hd.softeer.luckycardgame

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

class CardSectionAdapter(
    private val cardList: List<Card>,
    private val cardWidth: Int,
    private val cardHeight: Int,
    private val playerNum: Int
) :
    RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding =
            when (viewType) {
                0 -> {
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
        Log.d("onCreateViewHolder", binding.root.toString())
        return if (viewType == 0) CardFrontViewHolder(binding as RvItemCardFrontBinding) else CardBackViewHolder(
            binding as RvItemCardBackBinding
        )
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return playerNum
    }


}

abstract class CardViewHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: Card)
}

class CardBackViewHolder(private val binding: RvItemCardBackBinding) :
    CardViewHolder(binding) {
    override fun bind(card: Card) {

    }
}

class CardFrontViewHolder(private val binding: RvItemCardFrontBinding) :
    CardViewHolder(binding) {
    override fun bind(card: Card) {
        binding.apply {
            tvAnimalUnicode.text = card.type.emoji
            tvCardNumberTopLeft.text = card.number.num.toString()
            tvCardNumberBottomRight.text = card.number.num.toString()
        }
        Log.d("onBind", card.toString())
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
