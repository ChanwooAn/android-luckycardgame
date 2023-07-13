package hd.softeer.luckycardgame.model.card

data class CardNumber(val num:Int){
    init {
        if (num !in 1..12) {
            throw IllegalArgumentException("Invalid card number")
        }
    }
    fun getCardNumber():String{
        return String.format("%02d",num)
    }
}
/*
    cardNumber class를 따로 만들어 1~12 이외의 숫자로 초기화하지 못하도록 예외처리하였고, 요구사항과 같이 2글자씩 print할 수 있는 함수를 구성하였습니다.
 */
