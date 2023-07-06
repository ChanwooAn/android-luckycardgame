package hd.softeer.luckycardgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hd.softeer.luckycardgame.model.Animal
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.CardNumber
import hd.softeer.luckycardgame.model.User

class MainActivityViewModel: ViewModel() {
    private val totalCardList: MutableList<Card> = mutableListOf()

    private val _playersCardList = MutableLiveData<MutableList<User>>()
    val playersCardList: LiveData<MutableList<User>> get()=_playersCardList

    private val _sharedCardList = MutableLiveData<MutableList<Card>>()
    val sharedCardList:LiveData<MutableList<Card>> get()=_sharedCardList


    private var _cardWidth = 0
    val cardWidth get()=_cardWidth

    private var _cardHeight=0
    val cardHeight get()=_cardHeight

    init{
        _playersCardList.value= mutableListOf()
        _sharedCardList.value= mutableListOf()
    }

    fun initCardList(playerNum: Int) {
        totalCardList.clear()
        makeCardRandom(getMaxCardNum(playerNum))
        divideCardToPlayers(playerNum)
    }

    fun setCardWidth(scale:Float,parentPixelWidth:Int){
        val parentDpWidth = (parentPixelWidth/ scale + 0.5f).toInt() // Convert pixels to dp
        val cardDpWidth=parentDpWidth/6-8
        val cardPixelWidth = (cardDpWidth * scale + 0.5f).toInt() // Convert dp to pixels

        _cardWidth=cardPixelWidth
    }
    fun setCardHeight(scale:Float,parentPixelHeight:Int){
        val parentDpHeight = (parentPixelHeight/ scale + 0.5f).toInt()
        val cardDpHeight=parentDpHeight-16
        val cardPixelHeight = (cardDpHeight * scale + 0.5f).toInt()

        _cardHeight=cardPixelHeight
    }

    private fun makeCardRandom(cardMaxNumber: Int) {
        for (type in Animal.values()) {
            for (number in 1..cardMaxNumber) {
                totalCardList.add(Card(type, CardNumber(number)))
            }
        }
        totalCardList.shuffle()
    }

    private fun divideCardToPlayers(playerNum:Int){
        val cardsNumPerPlayer=11-playerNum
        _playersCardList.value!!.clear()
        _sharedCardList.value!!.clear()

        var start=0
        var end=cardsNumPerPlayer-1
        for(i in 0 until playerNum){
            _playersCardList.value!!.add(User((totalCardList.slice(start..end))))
            start+=cardsNumPerPlayer
            end+=cardsNumPerPlayer
        }

        _sharedCardList.value!!.addAll(totalCardList.slice(start..totalCardList.lastIndex))

    }

    private fun getMaxCardNum(playerNum: Int): Int {
        return if (playerNum == 3) 11 else 12
    }
}