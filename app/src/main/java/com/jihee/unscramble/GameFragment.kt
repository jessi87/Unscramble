package com.jihee.unscramble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jihee.unscramble.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private val viewModel by viewModels<GameViewModel> ()

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
        root
    }
}