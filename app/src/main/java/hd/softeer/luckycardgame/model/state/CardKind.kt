package hd.softeer.luckycardgame.model.state


sealed class CardKind {
    object PlayerCard : CardKind()
    object SharedCard : CardKind()
}
//sharedcard