package com.jihee.unscramble

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val model = WordList() // 모델생성

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score //MutableLiveData 값 참조

    private val _currentWordCount = MutableLiveData(0) // 라운드 수
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData("") // 출제된 단어
    val currentScrambledWords: LiveData<String>
        get() = _currentScrambledWord

    private var wordlist: MutableList<String> = mutableListOf() // 출제된 원본 단어 리스트
    private lateinit var currentword: String // 현재 원본 단어

    init {
        getNextWord()
    }

    //다음 단어를 요청하는 함수
    fun nextWord(): Boolean = when {
        _currentWordCount.value!! < MAX_NO_OF_WORDS -> {
            getNextWord()
            true
        }
        else -> false
    }

    private fun getNextWord() {
        currentword = model.words.random() // model에 있는 단어리스트에서 임의로 단어 가져옴
        val tempWord = currentword.toCharArray() // 단어의 각 글자들을 배열화
        tempWord.shuffle() //철자 뒤바꾸기

        while (tempWord.toString().equals(currentword, false)) { // 철자 순서를 바꿔도 기존단어랑 똑같으면 계속 섞기
            tempWord.shuffle()
        }

        when {
            wordlist.contains(currentword) -> getNextWord() // 중복된 단어일 경우 다시 재귀호출
            // 아닐경우 섞인단어를 변수에 저장, 라운드 수 증가, 단어리스트에 단어 추가
            else -> {
                _currentScrambledWord.value = String(tempWord)
                _currentWordCount.value = _currentWordCount.value?.inc()
                wordlist.add(currentword)
            }
        }
    }

    // 게임을 재시작할때 데이터를 초기화하는 함수
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordlist.clear()
        getNextWord()
    }

    fun isUserWordCorrect(playerword: String): Boolean = when {
        playerword.equals(currentword, false) -> {
            _score.value = _score.value?.plus(SCORE_INCREASE) // _score 는 nullable 타입이니까 처리해줘야됨
            //  _score.value = _score.value!! + SCORE_INCREASE
            true
        }
        else -> false
    }


    //클래스 상수
    companion object {
        const val MAX_NO_OF_WORDS = 10
        const val SCORE_INCREASE = 20
    }

}
