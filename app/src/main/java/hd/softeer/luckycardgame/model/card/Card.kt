package hd.softeer.luckycardgame.model.card

data class Card(
    val type: Animal,
    val number: CardNumber,
    var state: CardState
) {

    fun getCardInfo(): String {
        return "${type.emoji}${number.getCardNumber()}"
    }

}

enum class CardState(cardState: Int) {
    CARD_OPEN(100),
    CARD_CLOSE(101)
}
