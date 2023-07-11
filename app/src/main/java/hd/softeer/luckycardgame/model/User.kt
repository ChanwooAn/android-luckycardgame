package hd.softeer.luckycardgame.model

data class User(
    val cardList: MutableList<Card>,
    var turningCount: Int,
    val acquiredCardList: MutableSet<Int>
)