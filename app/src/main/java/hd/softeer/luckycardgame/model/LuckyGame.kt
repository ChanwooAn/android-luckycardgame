package hd.softeer.luckycardgame.model


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
                totalCardList.add(Card(type, CardNumber(number), CardState.CARD_CLOSE))
            }
        }
        totalCardList.shuffle()

        val cardsNumPerPlayer = 11 - playerNum
        var start = 0
        var end = cardsNumPerPlayer - 1
        val cardLists = mutableListOf<MutableList<Card>>()
        for (i in 0 until playerNum) {
            cardLists.add((totalCardList.slice(start..end)).toMutableList())
            start += cardsNumPerPlayer
            end += cardsNumPerPlayer
        }

        for (cardList in cardLists) {
            sortCardAsc(cardList)
        }

        gameInfo =
            GameInfo.initGameInfo(cardLists, totalCardList.slice(start..totalCardList.lastIndex))
    }

    private fun isCardsSame(card1: Card, card2: Card, card3: Card): Boolean {
        return card1.number.num == card2.number.num && card2.number.num == card3.number.num
    }

    private fun sortCardAsc(cardList: MutableList<Card>) {
        return cardList.sortBy { it.number.num }
    }



}