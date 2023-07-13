package hd.softeer.luckycardgame.model.card

enum class Animal(val emoji: String) {
    Dog("🐶"),
    Cat("🐱"),
    Cow("🐮")
}
/*
 animal type은 3개로 국한되므로 예외방지를 위해 sealed class 나 enum class 를 고려할 수 있습니다.
 일반적으로 sealed class를 많이 사용하지만 전체 카드를 생성하는 과정에서 iterate를 해야할 것 같아 enum class를 사용하였습니다.
 */
