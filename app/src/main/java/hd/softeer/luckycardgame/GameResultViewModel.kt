package hd.softeer.luckycardgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hd.softeer.luckycardgame.model.WinnersInfo
import hd.softeer.luckycardgame.model.card.Animal
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.card.CardNumber
import hd.softeer.luckycardgame.model.card.CardState

class GameResultViewModel : ViewModel() {

    private var _cardWidth = 0
    val cardWidth get() = _cardWidth

    private var _cardHeight = 0
    val cardHeight get() = _cardHeight

    private val _playersList = MutableLiveData<List<List<Card>>>()
    val playersList: LiveData<List<List<Card>>> get() = _playersList

    var winnersNameList = listOf<String>()

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

    fun updateWinnersInfo(winnersInfo: List<WinnersInfo>) {
        _playersList.value = winnersInfo.map {
            it.cardsNumbers.flatMap { cardNumber ->
                listOf(
                    Card(
                        Animal.Cat,
                        CardNumber(cardNumber), CardState.CARD_OPEN
                    ),
                    Card(
                        Animal.Cow,
                        CardNumber(cardNumber), CardState.CARD_OPEN
                    ),
                    Card(
                        Animal.Dog,
                        CardNumber(cardNumber), CardState.CARD_OPEN
                    )
                )
            }
        }



    }
}