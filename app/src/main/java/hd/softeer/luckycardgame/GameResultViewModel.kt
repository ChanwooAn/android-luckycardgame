package hd.softeer.luckycardgame

import androidx.lifecycle.ViewModel

class GameResultViewModel: ViewModel() {

    private var _cardWidth = 0
    val cardWidth get() = _cardWidth

    private var _cardHeight = 0
    val cardHeight get() = _cardHeight


    fun setCardWidth(scale: Float, parentPixelWidth: Int) {
        val parentDpWidth = (parentPixelWidth / scale + 0.5f).toInt() // Convert pixels to dp
        val cardDpWidth = parentDpWidth / 6 - 8
        val cardPixelWidth = (cardDpWidth * scale + 0.5f).toInt() // Convert dp to pixels

        _cardWidth = cardPixelWidth
    }

    fun setCardHeight(scale: Float, parentPixelHeight: Int) {
        val parentDpHeight = (parentPixelHeight / scale + 0.5f).toInt()
        val cardDpHeight = parentDpHeight - 16
        val cardPixelHeight = (cardDpHeight * scale + 0.5f).toInt()

        _cardHeight = cardPixelHeight
    }
}