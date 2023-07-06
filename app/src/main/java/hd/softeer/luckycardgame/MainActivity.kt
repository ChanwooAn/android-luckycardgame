package hd.softeer.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hd.softeer.luckycardgame.databinding.ActivityMainBinding
import hd.softeer.luckycardgame.model.Animal
import hd.softeer.luckycardgame.model.Card
import hd.softeer.luckycardgame.model.CardNumber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }
    private lateinit var card_section_tv_list: List<TextView> //iteration을 위해 각 카드 영역의 textview와 recycler view를 list로 관리.
    private lateinit var card_section_rv_list: List<RecyclerView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        card_section_rv_list = listOf(
            binding.rvCardSectionA,
            binding.rvCardSectionB,
            binding.rvCardSectionC,
            binding.rvCardSectionD,
            binding.rvCardSectionE
        )



        card_section_tv_list = listOf(
            binding.tvCardSectionA,
            binding.tvCardSectionB,
            binding.tvCardSectionC,
            binding.tvCardSectionD,
            binding.tvCardSectionE
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


                binding.tvCardSectionA.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }


    companion object {
        private const val TAG = "Main Activity"

    }
}