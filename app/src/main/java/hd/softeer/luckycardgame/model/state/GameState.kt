package hd.softeer.luckycardgame.model.state

sealed class GameState {
    object GameEndWithWinners : GameState()
    object GameEndWithNoWinners : GameState()
    object GameNotEnd : GameState()
    object BonusCardStage : GameState()
}