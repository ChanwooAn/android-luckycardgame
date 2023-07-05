package hd.softeer.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hd.softeer.luckycardgame.databinding.ActivityMainBinding
import hd.softeer.luckycardgame.model.Animal
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.CardNumber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createCardList()
        printCardList()
    }

    private lateinit var cardList:MutableList<Card>
    private fun createCardList(){
        cardList= mutableListOf()
        for(type in Animal.values()){
            for(number in 1..12){
                cardList.add(Card(type, CardNumber(number)))
            }
        }
        cardList.shuffle()
    }
    private fun printCardList(){
        val cardListString= mutableListOf<String>()
        for(card in cardList){
            cardListString.add(card.getCardInfo())
        }
        Log.d(TAG,cardListString.joinToString(", "))
    }


    companion object{
        private const val TAG="Main Activity"

    }
}