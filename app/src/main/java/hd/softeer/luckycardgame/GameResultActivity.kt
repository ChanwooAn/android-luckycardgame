package hd.softeer.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import hd.softeer.luckycardgame.adapter.CardSectionAdapter
import hd.softeer.luckycardgame.adapter.decorator.PlayerCardItemDecorator
import hd.softeer.luckycardgame.adapter.decorator.ResultCardDecorator
import hd.softeer.luckycardgame.databinding.ActivityGameResultBinding

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

        observeCardSize()

    }

    /**
     * card가 담기는 영역의 크기인 textView의 size를 관찰하여 적절하게 카드 size가 설정될 수 있도록 한다.
     */
    private fun observeCardSize() {
        binding.tvCardSectionA.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val scale = resources.displayMetrics.density // Get the screen density scale
                viewModel.setCardWidth(scale, binding.tvCardSectionA.width)
                viewModel.setCardHeight(scale, binding.tvCardSectionA.height)

                initCardRecyclerView()

                binding.tvCardSectionA.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun initCardRecyclerView() {
        card_section_rv_list.forEach {
            it.layoutManager =
                LinearLayoutManager(this@GameResultActivity, RecyclerView.HORIZONTAL, false)
            it.addItemDecoration(ResultCardDecorator())
        }
    }

    private fun setCardRecyclerView(playerNum: Int) {
        card_section_rv_list[playerNum].apply {
            val cardAdapter = CardSectionAdapter(
                playerNum,
                viewModel.playersList.value!![playerNum].cardList,
                viewModel.cardWidth,
                viewModel.cardHeight,
                onUserCardClickedInfoUpdateCallback,
                isTurnCountLeft
            )
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }

    }

}