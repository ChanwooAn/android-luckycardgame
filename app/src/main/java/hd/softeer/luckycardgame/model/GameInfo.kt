package hd.softeer.luckycardgame.model

data class GameInfo(
    val users: List<User>,
    val sharedCardList: List<Card>
) {

    companion object {
        fun initGameInfo(
            userCardLists: List<List<Card>>,
            sharedCardList: List<Card>
        ): GameInfo {
            return GameInfo(users = userCardLists.map {
                User(it.toMutableList())
            }, sharedCardList = sharedCardList)
        }
    }
}