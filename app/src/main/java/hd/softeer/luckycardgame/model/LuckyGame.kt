package hd.softeer.luckycardgame.model

import kotlin.math.absoluteValue


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

    private fun checkWinners() {
        for (player in gameInfo.users.indices) {
            if (gameInfo.users[player].acquiredCardList.contains(7)) {
                gameInfo.winners.add(player)
            }
        }

        for (player1 in gameInfo.users.indices) {
            if (gameInfo.users[player1].acquiredCardList.isEmpty()) {
                continue
            }
            for (player2 in gameInfo.users.indices) {
                if (player1 == player2) {
                    continue
                }
                if (gameInfo.users[player2].acquiredCardList.isEmpty()) {
                    continue
                }
                if (canMakeSeven(player1, player2)) {
                    gameInfo.winners.addAll(listOf(player1, player2))
                }
            }
        }

    }

    private fun canMakeSeven(player1: Int, player2: Int): Boolean {
        for (card1 in gameInfo.users[player1].acquiredCardList) {
            for (card2 in gameInfo.users[player2].acquiredCardList) {
                if (card1 + card2 == 7) {
                    return true
                }
                if ((card1 - card2).absoluteValue == 7) {
                    return true
                }
            }
        }

        return false
    }

    fun updateGameInfo(position: Int, userId: Int) {
        val targetUser = gameInfo.users[userId]

        with(targetUser) {
            cardList[position].state = CardState.CARD_OPEN
            turningCount++
            if (checkTriple(position, userId)) {
                acquiredCardList.add(cardList[position].number.num)
                checkWinners()//승자 체크해서 플레이어 넘버를 추가시켜서 관리하고,
            }
        }

        if (isTurnEnd()) {
            initiateTurnCnt()
        }
    }


    private fun isTurnEnd(): Boolean {
        for (user in gameInfo.users) {
            if (user.turningCount < 3) {
                return false
            }
        }

        return true
    }

    private fun initiateTurnCnt() {
        for (user in gameInfo.users) {
            user.turningCount = 0
        }
    }

    /**
     * winner가 존재하면 true를 return
     */
    private fun getWinnerState(): Boolean {
        return gameInfo.winners.isNotEmpty()
    }

    /**
     * winner가 존재하고, 모든 플레이어가 턴을 마무리 했을 때 true를 return
     */
    fun getEndState(): Boolean {
        val isAllPlayerEnd = gameInfo.users.find { it.turningCount != 3 }
        return getWinnerState() && isAllPlayerEnd == null
    }

    fun getWinnersNumber(): List<Int> {
        return gameInfo.winners
    }

    fun isTurnCountLeft(userId: Int): Boolean {
        return gameInfo.users[userId].turningCount < 3
    }

    private fun checkTriple(cardPosition: Int, userId: Int): Boolean {
        val userCardList = gameInfo.users[userId].cardList
        val tripleCase = listOf(
            listOf(cardPosition - 1, cardPosition, cardPosition + 1),
            listOf(cardPosition - 2, cardPosition - 1, cardPosition),
            listOf(cardPosition, cardPosition + 1, cardPosition + 2)
        )
        var isSame = false

        fun isPositionValidate(cardPosition: Int): Boolean {
            if (cardPosition < 0 || cardPosition > userCardList.size - 1) {
                return false
            }

            if (userCardList[cardPosition].state != CardState.CARD_OPEN) {
                return false
            }

            return true
        }

        for ((first, second, third) in tripleCase) {
            if (isPositionValidate(first) && isPositionValidate(second) && isPositionValidate(third) && isCardsSame(
                    userCardList[first],
                    userCardList[second],
                    userCardList[third]
                )
            ) {
                isSame = true
            }
        }

        return isSame
    }


}