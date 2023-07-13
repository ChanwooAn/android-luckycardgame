package hd.softeer.luckycardgame.model.game

import hd.softeer.luckycardgame.model.card.Card

data class GameInfo(
    val users: List<User>,
    val sharedCardList: List<Card>,
    var turn: Int,
    val winners: MutableList<Int>,
    var userNow:Int
) {

    companion object {
        fun initGameInfo(
            userCardLists: List<List<Card>>,
            sharedCardList: List<Card>
        ): GameInfo {
            return GameInfo(users = userCardLists.map {
                User(it.toMutableList(), 0, mutableSetOf())
            }, sharedCardList = sharedCardList, turn = 0, winners = mutableListOf(),0)
        }
    }
}