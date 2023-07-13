package hd.softeer.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hd.softeer.luckycardgame.adapter.CardSectionAdapter
import hd.softeer.luckycardgame.adapter.SharedCardsAdapter
import hd.softeer.luckycardgame.adapter.decorator.PlayerCardItemDecorator
import hd.softeer.luckycardgame.adapter.decorator.SharedCardItemDecorator
import hd.softeer.luckycardgame.databinding.ActivityMainBinding
import hd.softeer.luckycardgame.model.state.CardKind
import hd.softeer.luckycardgame.model.state.GameState


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    private val onUserCardClickedInfoUpdateCallback = { position: Int, cardKind: CardKind ->
        viewModel.updateCardState(position, cardKind)
    }
    private val isTurnCountLeft = { userId: Int ->
        viewModel.isMyTurn(userId) && viewModel.isTurnCountLeft(userId)
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
        observeGameEndState()
        setPlayerNumberButtonListener()

    }

    /**
     * segmented button을 눌렀을 때 player 수를 setting 하고 카드를 초기화하여 나눠주는 등의 작업을 수행한다.
     */
    private fun setPlayerNumberButtonListener() {
        binding.segbtnSelectNumOfPlayer.addOnButtonCheckedListener { group, checkedId, isChecked ->
            removeCardListObserver()
            when (checkedId) {
                R.id.bt_player_three -> {
                    viewModel.startGame(3)
                    initViewByPlayerNum(3)
                }

                R.id.bt_player_four -> {
                    viewModel.startGame(4)
                    initViewByPlayerNum(4)
                }

                R.id.bt_player_five -> {
                    viewModel.startGame(5)
                    initViewByPlayerNum(5)
                }
            }
            observeCardList()
        }//각 버튼에 맞는 인원수를 viewmodel에 전달하여 카드 list를 적절히 초기화하고, observe하여 view를 update한다.
    }

    private fun removeCardListObserver() {
        viewModel.playersList.removeObservers(this)
    }

    private fun observeCardList() {
        val playerCardList = viewModel.playersList.value!!
        viewModel.playersList.observe(this) {
            for (playerNum in playerCardList.indices) {
                if (playerCardList[playerNum].cardList.isNotEmpty()) {
                    card_section_tv_list[playerNum].text = ""
                    binding.bottomSection.text = ""
                } else {
                    card_section_tv_list[playerNum].text = getCardSectionText(playerNum)
                    binding.bottomSection.text = getString(R.string.initial_explain)

                }//card list가 비었을 때만 textView에서 text표시
                setCardRecyclerView(playerNum)
            }
        }
        viewModel.sharedCardList.observe(this) {
            setSharedCardRecyclerView(playerCardList.size)
        }
    }

    private fun observeGameEndState() {
        viewModel.gameState.observe(this) {
            when (it) {
                GameState.GameEndWithWinners -> {
                    Log.d(
                        TAG, "Game Ended winners:${viewModel.getWinnersNumber().joinToString(" ")}"
                    )
                }

                GameState.GameEndWithNoWinners -> {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.toast_game_end_with_no_winners), Toast.LENGTH_SHORT
                    ).show()
                }

                GameState.BonusCardStage -> {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.toast_bonus_stage_explain),
                        Toast.LENGTH_SHORT
                    ).show()


                }

                else -> {}
            }
        }
    }


    private fun getCardSectionText(playerNum: Int): String {
        return when (playerNum) {
            0 -> {
                getString(R.string.card_section_A_text)
            }

            1 -> {
                getString(R.string.card_section_B_text)
            }

            2 -> {
                getString(R.string.card_section_C_text)
            }

            3 -> {
                getString(R.string.card_section_D_text)
            }

            4 -> {
                getString(R.string.card_section_E_text)
            }

            else -> {
                ""
            }
        }
    }

    private fun initCardRecyclerView() {
        card_section_rv_list.forEach {
            it.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            it.addItemDecoration(PlayerCardItemDecorator(viewModel.cardWidth))
        }
    }//player의 카드영역은 player의 수에 따라 바뀌는게 없으므로 따로 빼주어 최초 한 번만 layoutmanager와 item decorator 를 설정한다.

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

    private fun setSharedCardRecyclerView(playerNum: Int) {
        val gridSpan = when (playerNum) {
            4 -> {
                4
            }

            5 -> {
                6
            }

            else -> {
                5
            }
        }

        binding.rvCardShared.apply {
            val cardAdapter = SharedCardsAdapter(
                viewModel.sharedCardList.value!!,
                viewModel.cardWidth,
                viewModel.cardHeight,
            ) { position: Int, cardKind: CardKind ->
                viewModel.updateCardState(position, cardKind)
            }
            adapter = cardAdapter
            layoutManager = GridLayoutManager(this@MainActivity, gridSpan)
            for (i in 0 until itemDecorationCount) {
                val decoration = getItemDecorationAt(i)
                if (decoration is SharedCardItemDecorator) {
                    removeItemDecoration(decoration)
                }
            }
            addItemDecoration(SharedCardItemDecorator(gridSpan))
        }
    }

    /**
     * 인원 수에 맞게 card 영역의 visibility를 적절히 초기화한다.
     */
    private fun initViewByPlayerNum(playerNum: Int) {
        when (playerNum) {
            3 -> {
                for (i in 0 until 3) {
                    card_section_rv_list[i].visibility = View.VISIBLE
                    card_section_tv_list[i].visibility = View.VISIBLE
                }

                card_section_tv_list[3].visibility = View.INVISIBLE
                card_section_rv_list[3].visibility = View.INVISIBLE

                card_section_tv_list[4].visibility = View.GONE
                card_section_rv_list[4].visibility = View.GONE
            }

            4 -> {
                for (i in 0 until 4) {
                    card_section_rv_list[i].visibility = View.VISIBLE
                    card_section_tv_list[i].visibility = View.VISIBLE
                }
                card_section_tv_list[4].visibility = View.GONE
                card_section_rv_list[4].visibility = View.GONE
            }

            5 -> {
                card_section_rv_list.forEach {
                    it.visibility = View.VISIBLE
                }
                card_section_tv_list.forEach {
                    it.visibility = View.VISIBLE
                }
            }
        }
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


    companion object {
        private const val TAG = "Main Activity"
        private const val SHARED_CARD_SECTION_ID = 1000
    }


}