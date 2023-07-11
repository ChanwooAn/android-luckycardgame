package hd.softeer.luckycardgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.LuckyGame
import hd.softeer.luckycardgame.model.User

class MainActivityViewModel : ViewModel() {

    private val gameManager = LuckyGame()

    private val _playersList = MutableLiveData<List<User>>()
    val playersList: LiveData<List<User>> get() = _playersList

    private val _sharedCardList = MutableLiveData<List<Card>>()
    val sharedCardList: LiveData<List<Card>> get() = _sharedCardList


    private var _cardWidth = 0
    val cardWidth get() = _cardWidth

    private var _cardHeight = 0
    val cardHeight get() = _cardHeight

    private val _endFlg = MutableLiveData<Boolean>()
    val endFlg: LiveData<Boolean> get() = _endFlg


    init {
        _playersList.value = mutableListOf()
        _sharedCardList.value = mutableListOf()
        _endFlg.value = false
    }

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

    fun startGame(playerNumber: Int) {
        gameManager.initGame(playerNumber)
        _playersList.value = gameManager.getPlayersCardInfo()
        _sharedCardList.value = gameManager.getSharedCardInfo()
    }

    fun updateCardState(position: Int, userId: Int) {
        gameManager.updateGameInfo(position, userId)
        _endFlg.value = gameManager.getEndState()
    }

    fun isTurnCountLeft(userId: Int): Boolean {
        return gameManager.isTurnCountLeft(userId)
    }

    fun getWinnersNumber(): List<Int> {
        return gameManager.getWinnersNumber()
    }


}