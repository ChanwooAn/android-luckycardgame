package hd.softeer.luckycardgame.model.game

import hd.softeer.luckycardgame.model.card.Card

data class User(
    val cardList: MutableList<Card>,
    var turningCount: Int,
    val acquiredCardList: MutableSet<Int>
)