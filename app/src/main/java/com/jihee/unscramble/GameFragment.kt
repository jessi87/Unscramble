package com.jihee.unscramble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jihee.unscramble.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private val viewModel by viewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DataBindingUtil.inflate<FragmentGameBinding>(
        inflater,
        R.layout.fragment_game,
        container,
        false
    ).run {
        lifecycleOwner = viewLifecycleOwner //양방향 데이터바인딩을 위함
        bindingViewData()
        setEventListener()
        root
    }

    private fun FragmentGameBinding.bindingViewData() {
        vm = viewModel
    }

    private fun FragmentGameBinding.setEventListener() {
        viewSubmit.setOnClickListener {
            viewInputField.editText?.text.toString().let { word ->
                when(viewModel.isUserWordCorrect(word)) {
                    true -> {
                        setErrorTextField(false)
                        if (!viewModel.nextWord()) {
                            showFinalScoreDialog()
                        }
                    }
                    false -> {
                        setErrorTextField(true)

                    }
                }
            }
        }

        viewSkip.setOnClickListener {
            when {
                viewModel.nextWord() -> setErrorTextField(false)
                else -> showFinalScoreDialog()
            }
        }
    }
    private fun FragmentGameBinding.setErrorTextField(error: Boolean) {
        viewInputField.isErrorEnabled = error

        when{
            error -> viewInputField.error = getString(R.string.try_again)
            else -> viewInputField.editText?.text = null
        }
    }

    private fun FragmentGameBinding.showFinalScoreDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored,viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)){_,_ -> exitGame()}
            .setPositiveButton(getString(R.string.play_again)){_,_ -> restartGame()}
            .show()

    private fun FragmentGameBinding.restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    private fun exitGame() {
        activity?.finish()
    }
}

