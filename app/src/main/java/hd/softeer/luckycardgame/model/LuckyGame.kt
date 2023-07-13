package hd.softeer.luckycardgame.model

import android.util.Log
import hd.softeer.luckycardgame.model.card.Animal
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.card.CardNumber
import hd.softeer.luckycardgame.model.card.CardState
import hd.softeer.luckycardgame.model.game.GameInfo
import hd.softeer.luckycardgame.model.state.GameState
import hd.softeer.luckycardgame.model.game.User
import hd.softeer.luckycardgame.model.state.CardKind
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

    fun updateGameInfo(position: Int, cardKind: CardKind) {
        when (cardKind) {
            CardKind.PlayerCard -> {
                updateUserCardState(position)
            }

            CardKind.SharedCard -> {
                updateSharedCardState(position)
            }
        }
        checkWinners()
        updateTurnState()
    }

    private fun updateUserCardState(position: Int) {
        val targetUserId = gameInfo.userNow
        val targetUser = gameInfo.users[targetUserId]

        with(targetUser) {
            cardList[position].state = CardState.CARD_OPEN
            turningCount++
            updateAcquiredCardList(position)
        }//유저의 카드 뒤집은 횟수 올리고 승리 조건 달성자 있는지 검사
    }


    private fun updateSharedCardState(position: Int) {
        val userNow = gameInfo.users[gameInfo.userNow]
        Log.d("SharedCardState", gameInfo.users.map { it.turningCount }.joinToString(" "))
        gameInfo.sharedCardList[position].state = CardState.CARD_OPEN
        with(userNow) {
            turningCount++
            cardList.add(gameInfo.sharedCardList[position])
            sortCardAsc(this.cardList)
            for (i in 1 until userNow.cardList.size) {
                updateAcquiredCardList(i)
            }
        }

    }

    private fun updateAcquiredCardList(cardPosition: Int) {
        val userNumber = gameInfo.userNow
        val card = gameInfo.users[userNumber].cardList[cardPosition]
        if (checkTriple(cardPosition, userNumber)) {
            gameInfo.users[userNumber].acquiredCardList.add(card.number.num)
        }
    }

    private fun updateTurnState() {
        val userNow = gameInfo.users[gameInfo.userNow]
        if (userNow.turningCount % 3 == 0 || userNow.turningCount >= userNow.cardList.size) {
            gameInfo.userNow = (gameInfo.userNow + 1) % gameInfo.users.size
        }// user의 턴이 끝나면 현재 플레이하고 있는 유저의 정보를 다음사람으로 변경

        val turnEndCnt = (gameInfo.turn + 1) * (gameInfo.users.size * 3)
        if (gameInfo.users.sumOf { it.turningCount } == turnEndCnt) {
            gameInfo.turn++
        }//턴이 끝날 경우 update
    }


    /**
     * GameState를 return
     * 1. 승자가 발생하고, 모든 플레이어가 3장을 뒤집어 턴을 끝났을 경우 GameEndWithWinners를 return
     * 2. 플레이어가 가지고 있는 카드를 전부 뒤집었고 맨 밑의 칸에서 보너스 카드를 뽑아야 하는 상태일 때 BonusCardStage를 return
     * 3. 플레이어가 가지고 있는 카드를 전부 뒤집었고 보너스카드 까지 뽑았지만 승자가 발생하지 않았을 때 GameEndWithNoWinners를 return
     * 4. 게임이 진행중이라면 GameNotEnd를 return
     */
    fun getGameState(): GameState {
        val playerCardSize = getPlayerCardSize()

        val isAllPlayersCardsOpen =
            gameInfo.users.find { it.turningCount != playerCardSize } == null
        val isEndWithNoWinners =
            gameInfo.users.find { it.turningCount != playerCardSize + 1 } == null
        val isThisTurnEnd = (gameInfo.users.find { it.turningCount % 3 != 0 }) == null
        val isWinnerExist = gameInfo.winners.isNotEmpty()
        Log.d(
            "GameState", "$isAllPlayersCardsOpen $isEndWithNoWinners $isThisTurnEnd $isWinnerExist"
        )
        return if (isWinnerExist && isThisTurnEnd) {
            GameState.GameEndWithWinners
        } else if (isAllPlayersCardsOpen) {
            GameState.BonusCardStage
        } else if (isEndWithNoWinners) {
            GameState.GameEndWithNoWinners
        } else {
            GameState.GameNotEnd
        }
    }

    private fun getPlayerCardSize(): Int {
        return when (gameInfo.users.size) {
            3 -> PLAYER_CARD_SIZE_CASE_THREE
            4 -> PLAYER_CARD_SIZE_CASE_FOUR
            else -> PLAYER_CARD_SIZE_CASE_FIVE
        }
    }


    fun getWinnersNumber(): List<Int> {
        return gameInfo.winners
    }

    fun isTurnCountLeft(userId: Int): Boolean {
        return (gameInfo.users[userId].turningCount / 3) == gameInfo.turn
    }

    fun isMyTurn(userId: Int): Boolean {
        return gameInfo.userNow == userId
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
                    userCardList[first], userCardList[second], userCardList[third]
                )
            ) {
                isSame = true
            }
        }

        return isSame
    }

    companion object {
        private const val PLAYER_CARD_SIZE_CASE_THREE = 8
        private const val PLAYER_CARD_SIZE_CASE_FOUR = 7
        private const val PLAYER_CARD_SIZE_CASE_FIVE = 6
    }


}