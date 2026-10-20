package com.example.practice_victorina

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

/**
 * Экран прохождения викторины (QuizActivity):
 * - Загружает выбранную викторину из JSON в assets
 * - Отображает вопросы и 4 варианта ответов
 * - Подсчитывает число верных ответов
 * - Передает результат в ResultActivity через явный Intent
 */
class QuizActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "QuizActivity"
    }

    private lateinit var tvQuizTitle: TextView
    private lateinit var tvPlayerName: TextView
    private lateinit var pbQuestionProgress: ProgressBar
    private lateinit var tvQuestionCounter: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var btnAction: Button

    private var playerName: String = ""
    private var quizFile: String = ""
    private var quizTitle: String = ""

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex: Int = 0
    private var selectedOptionIndex: Int = -1
    private var score: Int = 0
    private var isAnswerConfirmed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        Log.d(TAG, "onCreate: экран создан")

        playerName = intent.getStringExtra(MainActivity.EXTRA_PLAYER_NAME) ?: "Гость"
        quizFile = intent.getStringExtra(MainActivity.EXTRA_QUIZ_FILE) ?: "quiz_space.json"
        quizTitle = intent.getStringExtra(MainActivity.EXTRA_QUIZ_TITLE) ?: "Викторина"

        Log.i(TAG, "Запуск викторины для игрока '$playerName' по теме '$quizTitle'")

        initViews()
        loadQuestions()
    }

    private fun initViews() {
        tvQuizTitle = findViewById(R.id.tvQuizTitle)
        tvPlayerName = findViewById(R.id.tvPlayerName)
        pbQuestionProgress = findViewById(R.id.pbQuestionProgress)
        tvQuestionCounter = findViewById(R.id.tvQuestionCounter)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        btnAction = findViewById(R.id.btnAction)

        optionButtons = listOf(
            findViewById(R.id.btnOption0),
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3)
        )

        tvQuizTitle.text = quizTitle
        tvPlayerName.text = getString(R.string.quiz_player_label, playerName)

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (!isAnswerConfirmed) {
                    onOptionSelected(index)
                }
            }
        }

        btnAction.setOnClickListener {
            if (!isAnswerConfirmed) {
                confirmAnswer()
            } else {
                goToNextQuestion()
            }
        }
    }

    /**
     * Прямая загрузка вопросов из JSON в assets без дополнительных библиотек
     */
    private fun loadQuestions() {
        val jsonString = assets.open("quizzes/$quizFile").bufferedReader().use { it.readText() }
        val root = JSONObject(jsonString)
        val array = root.getJSONArray("questions")
        val list = mutableListOf<Question>()

        for (i in 0 until array.length()) {
            val q = array.getJSONObject(i)
            val id = q.getInt("id")
            val text = q.getString("question")
            val opts = q.getJSONArray("options")
            val optionsList = mutableListOf<String>()
            for (j in 0 until opts.length()) {
                optionsList.add(opts.getString(j))
            }
            val correct = q.getInt("correctIndex")
            list.add(Question(id, text, optionsList, correct))
        }

        questions = list
        pbQuestionProgress.max = questions.size
        Log.d(TAG, "Загружено вопросов: ${questions.size}")
        displayCurrentQuestion()
    }

    private fun displayCurrentQuestion() {
        if (currentQuestionIndex >= questions.size) return

        val currentQuestion = questions[currentQuestionIndex]
        selectedOptionIndex = -1
        isAnswerConfirmed = false
        btnAction.text = getString(R.string.btn_confirm_answer)

        pbQuestionProgress.progress = currentQuestionIndex + 1
        tvQuestionCounter.text = getString(R.string.quiz_progress_format, currentQuestionIndex + 1, questions.size)
        tvQuestionText.text = currentQuestion.question

        optionButtons.forEachIndexed { index, button ->
            button.isEnabled = true
            button.setBackgroundResource(R.drawable.bg_option_default)
            if (index < currentQuestion.options.size) {
                button.visibility = Button.VISIBLE
                button.text = currentQuestion.options[index]
            } else {
                button.visibility = Button.GONE
            }
        }
    }

    private fun onOptionSelected(selectedIndex: Int) {
        selectedOptionIndex = selectedIndex
        optionButtons.forEachIndexed { index, button ->
            if (index == selectedIndex) {
                button.setBackgroundResource(R.drawable.bg_option_selected)
            } else {
                button.setBackgroundResource(R.drawable.bg_option_default)
            }
        }
    }

    private fun confirmAnswer() {
        if (selectedOptionIndex == -1) {
            Log.w(TAG, "Попытка ответа без выбора варианта")
            Toast.makeText(this, getString(R.string.msg_select_option), Toast.LENGTH_SHORT).show()
            return
        }

        val currentQuestion = questions[currentQuestionIndex]
        val isCorrect = (selectedOptionIndex == currentQuestion.correctIndex)
        isAnswerConfirmed = true
        optionButtons.forEach { it.isEnabled = false }

        if (isCorrect) {
            score++
            optionButtons[selectedOptionIndex].setBackgroundResource(R.drawable.bg_option_correct)
        } else {
            optionButtons[selectedOptionIndex].setBackgroundResource(R.drawable.bg_option_wrong)
            if (currentQuestion.correctIndex < optionButtons.size) {
                optionButtons[currentQuestion.correctIndex].setBackgroundResource(R.drawable.bg_option_correct)
            }
        }

        Log.i(TAG, "Вопрос #${currentQuestionIndex + 1}: выбран вариант #$selectedOptionIndex (верно: $isCorrect)")

        if (currentQuestionIndex == questions.size - 1) {
            btnAction.text = getString(R.string.btn_finish_quiz)
        } else {
            btnAction.text = getString(R.string.btn_next_question)
        }
    }

    private fun goToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayCurrentQuestion()
        } else {
            Log.i(TAG, "Викторина завершена: результат $score из ${questions.size}")
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra(ResultActivity.EXTRA_PLAYER_NAME, playerName)
                putExtra(ResultActivity.EXTRA_QUIZ_TITLE, quizTitle)
                putExtra(ResultActivity.EXTRA_CORRECT_COUNT, score)
                putExtra(ResultActivity.EXTRA_TOTAL_COUNT, questions.size)
            }
            startActivity(intent)
            finish()
        }
    }

    // Методы жизненного цикла
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}
