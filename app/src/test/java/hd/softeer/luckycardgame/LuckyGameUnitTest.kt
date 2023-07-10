package hd.softeer.luckycardgame

import hd.softeer.luckycardgame.model.Animal
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.CardNumber
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before


class LuckyGameUnitTest {

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
    fun checkLucky_ReturnsTrueForThreeEqualCards() {
        val card1 = Card(Animal.Dog, CardNumber(3))
        val card2 = Card(Animal.Cat, CardNumber(3))
        val card3 = Card(Animal.Cow, CardNumber(3))

        val result = luckyGame.checkLucky(card1, card2, card3)

        assertEquals(true, result)
    }

    @Test
    fun checkLucky_ReturnsFalseForThreeDifferentCards() {
        val card1 = Card(Animal.Dog, CardNumber(3))
        val card2 = Card(Animal.Cat, CardNumber(5))
        val card3 = Card(Animal.Cow, CardNumber(2))

        val result = luckyGame.checkLucky(card1, card2, card3)

        assertEquals(false, result)
    }

    @Test
    fun sortCardAsc_SortsUserCardListInAscendingOrder() {
        val targetList = mutableListOf(
            Card(Animal.Dog, CardNumber(3)),
            Card(Animal.Cat, CardNumber(5)),
            Card(Animal.Cow, CardNumber(2))
        )
        luckyGame.sortCardAsc(targetList)

        val expectedCardList = listOf(
            Card(Animal.Cow, CardNumber(2)),
            Card(Animal.Dog, CardNumber(3)),
            Card(Animal.Cat, CardNumber(5))
        )
        assertEquals(expectedCardList, targetList)
    }
}
