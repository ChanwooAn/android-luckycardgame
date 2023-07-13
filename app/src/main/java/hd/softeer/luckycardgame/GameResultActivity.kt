package hd.softeer.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hd.softeer.luckycardgame.adapter.ResultCardsAdapter
import hd.softeer.luckycardgame.adapter.decorator.ResultCardDecorator
import hd.softeer.luckycardgame.databinding.ActivityGameResultBinding
import hd.softeer.luckycardgame.model.WinnersInfo

class GameResultActivity : AppCompatActivity() {

    private lateinit var card_section_rv_list: List<RecyclerView>
    private val viewModel by lazy {
        ViewModelProvider(this).get(GameResultViewModel::class.java)
    }

    private lateinit var binding: ActivityGameResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        card_section_rv_list = listOf(
            binding.rvCardSectionA,
            binding.rvCardSectionB,
            binding.rvCardSectionC,
            binding.rvCardSectionD,
            binding.rvCardSectionE
        )
        //winners랑 winners가 가지고 있는 acquire card number를 받아야 함.
        //card number를 받아 3가지 카드로 구성할 것.

        observeCardSize()
        val winnersInfo=intent.getParcelableArrayListExtra<WinnersInfo>(WINNERS_NUMBER)
        updateWinnersInfo(winnersInfo?:listOf<WinnersInfo>().toList())
        observeCardList()
    }
    private fun updateWinnersInfo(winnersInfo:List<WinnersInfo>){
        viewModel.updateWinnersInfo(winnersInfo)
    }

    private fun observeCardSize() {
        binding.tvCardSectionA.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val scale = resources.displayMetrics.density // Get the screen density scale
                viewModel.setCardWidth(scale, binding.tvCardSectionA.width)
                viewModel.setCardHeight(scale, binding.tvCardSectionA.height)

                initCardRecyclerView()
                observeCardList()
                binding.tvCardSectionA.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun observeCardList() {
        viewModel.playersList.observe(this) {
            setCardRecyclerView()
        }
    }

    private fun initCardRecyclerView() {
        card_section_rv_list.forEach {
            it.layoutManager =
                LinearLayoutManager(this@GameResultActivity, RecyclerView.HORIZONTAL, false)
            it.addItemDecoration(ResultCardDecorator())
        }
    }

    private fun setCardRecyclerView() {
        card_section_rv_list.forEachIndexed { idx, recyclerView ->
            val cardAdapter = ResultCardsAdapter(
                viewModel.playersList.value!![idx],
                viewModel.cardWidth,
                viewModel.cardHeight
            )
            recyclerView.adapter = cardAdapter
        }

    }

    companion object {
        private const val WINNERS_NUMBER="winners number"
    }

}