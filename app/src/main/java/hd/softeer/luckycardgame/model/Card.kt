package hd.softeer.luckycardgame.model

data class Card(val type: Animal, val number: CardNumber) {

    fun getCardInfo(): String {
        return "${type.emoji}${number.getCardNumber()}"
    }
}