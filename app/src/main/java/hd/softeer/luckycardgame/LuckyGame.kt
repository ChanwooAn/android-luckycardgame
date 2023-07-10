package hd.softeer.luckycardgame

import hd.softeer.luckycardgame.model.Animal
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.CardNumber
import hd.softeer.luckycardgame.model.GameInfo
import hd.softeer.luckycardgame.model.User

class LuckyGame {

    //player 정보와 card 정보를 game안에서 관리.
    //game info class 를 따로 만들어서 관리?
    private lateinit var gameInfo: GameInfo


    fun initGame(playerNum: Int) {
        initCardLists(playerNum)
    }

    fun getPlayersCardInfo(): List<User> {
        return gameInfo.users
    }

    fun getSharedCardInfo(): List<Card> {
        return gameInfo.sharedCardList
    }

    private fun initCardLists(playerNum: Int) {
        val totalCardList = mutableListOf<Card>()
        val cardMaxNumber = if (playerNum == 3) 11 else 12
        for (type in Animal.values()) {
            for (number in 1..cardMaxNumber) {
                totalCardList.add(Card(type, CardNumber(number)))
            }
        }
        totalCardList.shuffle()

        val cardsNumPerPlayer = 11 - playerNum
        var start = 0
        var end = cardsNumPerPlayer - 1
        val cardLists = mutableListOf<List<Card>>()
        for (i in 0 until playerNum) {
            cardLists.add((totalCardList.slice(start..end)))
            start += cardsNumPerPlayer
            end += cardsNumPerPlayer
        }
        gameInfo =
            GameInfo.initGameInfo(cardLists, totalCardList.slice(start..totalCardList.lastIndex))
    }

    fun checkLucky(card1: Card, card2: Card, card3: Card): Boolean {
        return card1.number.num == card2.number.num && card2.number.num == card3.number.num
    }

    fun sortCardAsc(cardList: MutableList<Card>) {
        return cardList.sortBy { it.number.num }
    }

}