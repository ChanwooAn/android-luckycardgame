package hd.softeer.luckycardgame

import hd.softeer.luckycardgame.model.card.Animal
import hd.softeer.luckycardgame.model.card.Card
import hd.softeer.luckycardgame.model.card.CardNumber
import hd.softeer.luckycardgame.model.card.CardState
import hd.softeer.luckycardgame.model.game.GameInfo
import hd.softeer.luckycardgame.model.LuckyGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Method


class LuckyGameTest {

    private lateinit var luckyGame: LuckyGame

    @Before
    fun setUp() {
        luckyGame = LuckyGame()
        luckyGame.initGame(3)
    }

    @Test
    fun initGame_InitGameInfoWithCorrectCardDistributions() {
        val playerNum = 3

        luckyGame.initGame(playerNum)
        val playersCardInfo = luckyGame.getPlayersCardInfo()
        val sharedCardInfo = luckyGame.getSharedCardInfo()

        assertEquals(playerNum, playersCardInfo.size)
        assertEquals(9, sharedCardInfo.size)

        val totalCards = playersCardInfo.flatMap { it.cardList } + sharedCardInfo
        assertEquals(33, totalCards.size)

        for (user in playersCardInfo) {
            assertEquals(8, user.cardList.size)
        }
    }

    @Test
    fun isCardsSame_ReturnsTrueForThreeEqualCards() {
        val card1 = Card(Animal.Dog, CardNumber(3), CardState.CARD_CLOSE)
        val card2 = Card(Animal.Dog, CardNumber(3), CardState.CARD_CLOSE)
        val card3 = Card(Animal.Dog, CardNumber(3), CardState.CARD_CLOSE)

        val targetMethod: Method = LuckyGame::class.java.getDeclaredMethod(
            "isCardsSame",
            Card::class.java,
            Card::class.java,
            Card::class.java
        )
        targetMethod.isAccessible = true
        val result = targetMethod.invoke(luckyGame, card1, card2, card3) as Boolean

        assertTrue(result)
    }

    @Test
    fun isCardsSame_ReturnsFalseForThreeDiffCards() {
        val card1 = Card(Animal.Dog, CardNumber(3), CardState.CARD_CLOSE)
        val card2 = Card(Animal.Cat, CardNumber(5), CardState.CARD_CLOSE)
        val card3 = Card(Animal.Cow, CardNumber(2), CardState.CARD_CLOSE)

        val targetMethod: Method = LuckyGame::class.java.getDeclaredMethod(
            "isCardsSame",
            Card::class.java,
            Card::class.java,
            Card::class.java
        )
        targetMethod.isAccessible = true
        val result = targetMethod.invoke(luckyGame, card1, card2, card3) as Boolean

        assertFalse(result)
    }

    @Test
    fun checkTriple_ReturnsTrueForThreeEqualCards() {
        val card1 = Card(Animal.Dog, CardNumber(3), CardState.CARD_OPEN)
        val card2 = Card(Animal.Cat, CardNumber(3), CardState.CARD_OPEN)
        val card3 = Card(Animal.Cow, CardNumber(3), CardState.CARD_OPEN)

        val gameInfoField = LuckyGame::class.java.getDeclaredField("gameInfo")
        gameInfoField.isAccessible = true

        val gameInfo = gameInfoField.get(luckyGame) as GameInfo

        gameInfo.users[0].cardList[0] = card1
        gameInfo.users[0].cardList[1] = card2
        gameInfo.users[0].cardList[2] = card3

        val targetMethod: Method = LuckyGame::class.java.getDeclaredMethod(
            "checkTriple",
            Int::class.java,
            Int::class.java,
        )
        targetMethod.isAccessible = true

        val result = targetMethod.invoke(luckyGame, 1, 0) as Boolean

        assertTrue(result)
    }

    @Test
    fun canMakeSeven_ReturnsFalseWithOnePlayers() {
        val cardList = listOf(
            Card(Animal.Dog, CardNumber(7), CardState.CARD_OPEN),
            Card(Animal.Cat, CardNumber(7), CardState.CARD_OPEN),
            Card(Animal.Cow, CardNumber(7), CardState.CARD_OPEN),
        )


        val gameInfoField = LuckyGame::class.java.getDeclaredField("gameInfo")
        gameInfoField.isAccessible = true

        val gameInfo = gameInfoField.get(luckyGame) as GameInfo
        for (cardNum in 0 until 3) {
            gameInfo.users[0].cardList[cardNum] = cardList[cardNum]
        }


        val targetMethod: Method = LuckyGame::class.java.getDeclaredMethod(
            "canMakeSeven",
            Int::class.java,
            Int::class.java,
        )
        targetMethod.isAccessible = true

        val result = targetMethod.invoke(luckyGame, 0, 1) as Boolean

        assertFalse(result)
    }

    @Test
    fun canMakeSeven_ReturnsFalseWithTwoPlayers() {
        val cardList = listOf(
            Card(Animal.Dog, CardNumber(3), CardState.CARD_OPEN),
            Card(Animal.Cat, CardNumber(3), CardState.CARD_OPEN),
            Card(Animal.Cow, CardNumber(3), CardState.CARD_OPEN),
            Card(Animal.Dog, CardNumber(2), CardState.CARD_OPEN),
            Card(Animal.Cat, CardNumber(2), CardState.CARD_OPEN),
            Card(Animal.Cow, CardNumber(2), CardState.CARD_OPEN)
        )


        val gameInfoField = LuckyGame::class.java.getDeclaredField("gameInfo")
        gameInfoField.isAccessible = true

        val gameInfo = gameInfoField.get(luckyGame) as GameInfo
        for (userIdx in 0 until 2) {
            for (cardNum in 0 until 3) {
                gameInfo.users[userIdx].cardList[cardNum] = cardList[userIdx * 3 + cardNum]
            }
        }

        val targetMethod: Method = LuckyGame::class.java.getDeclaredMethod(
            "canMakeSeven",
            Int::class.java,
            Int::class.java,
        )
        targetMethod.isAccessible = true

        val result = targetMethod.invoke(luckyGame, 0, 1) as Boolean

        assertFalse(result)
    }

    @Test
    fun canMakeSeven_ReturnsTrueWithTwoPlayers() {
        val cardList = listOf(
            Card(Animal.Dog, CardNumber(3), CardState.CARD_OPEN),
            Card(Animal.Cat, CardNumber(3), CardState.CARD_OPEN),
            Card(Animal.Cow, CardNumber(3), CardState.CARD_OPEN),
            Card(Animal.Dog, CardNumber(4), CardState.CARD_OPEN),
            Card(Animal.Cat, CardNumber(4), CardState.CARD_OPEN),
            Card(Animal.Cow, CardNumber(4), CardState.CARD_OPEN)
        )


        val gameInfoField = LuckyGame::class.java.getDeclaredField("gameInfo")
        gameInfoField.isAccessible = true

        val gameInfo = gameInfoField.get(luckyGame) as GameInfo
        for (userIdx in 0 until 2) {
            for (cardNum in 0 until 3) {
                gameInfo.users[userIdx].cardList[cardNum] = cardList[userIdx * 3 + cardNum]
            }
        }

        val targetMethod: Method = LuckyGame::class.java.getDeclaredMethod(
            "canMakeSeven",
            Int::class.java,
            Int::class.java,
        )
        targetMethod.isAccessible = true

        val result = targetMethod.invoke(luckyGame, 0, 1) as Boolean

        assertFalse(result)
    }

    @Test
    fun sortCardAsc_SortsUserCardListInAscendingOrder() {
        var prevCardNum: Int
        var nowCardNum: Int
        for (cardList in luckyGame.getPlayersCardInfo().map { it.cardList }) {
            for (cardIdx in 1..cardList.lastIndex) {
                prevCardNum = cardList[cardIdx - 1].number.num
                nowCardNum = cardList[cardIdx].number.num
                assertTrue(prevCardNum <= nowCardNum)
            }
        }
    }
}
