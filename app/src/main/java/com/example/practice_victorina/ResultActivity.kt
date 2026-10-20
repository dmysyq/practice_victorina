package com.example.practice_victorina

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ResultActivity"

        const val EXTRA_PLAYER_NAME = "EXTRA_PLAYER_NAME"
        const val EXTRA_QUIZ_TITLE = "EXTRA_QUIZ_TITLE"
        const val EXTRA_CORRECT_COUNT = "EXTRA_CORRECT_COUNT"
        const val EXTRA_TOTAL_COUNT = "EXTRA_TOTAL_COUNT"
    }

    private lateinit var tvResultPlayerName: TextView
    private lateinit var tvResultQuizTitle: TextView
    private lateinit var tvScoreNumber: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var tvVerdict: TextView
    private lateinit var btnPlayAgain: Button
    private lateinit var btnHome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        Log.d(TAG, "onCreate: экран создан")

        val playerName = intent.getStringExtra(EXTRA_PLAYER_NAME) ?: "Игрок"
        val quizTitle = intent.getStringExtra(EXTRA_QUIZ_TITLE) ?: "Викторина"
        val score = intent.getIntExtra(EXTRA_CORRECT_COUNT, 0)
        val total = intent.getIntExtra(EXTRA_TOTAL_COUNT, 1)

        Log.i(TAG, "Результаты игры: игрок='$playerName', тема='$quizTitle', верных=$score/$total")

        tvResultPlayerName = findViewById(R.id.tvResultPlayerName)
        tvResultQuizTitle = findViewById(R.id.tvResultQuizTitle)
        tvScoreNumber = findViewById(R.id.tvScoreNumber)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvVerdict = findViewById(R.id.tvVerdict)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        btnHome = findViewById(R.id.btnHome)

        val percentage = if (total > 0) (score * 100) / total else 0

        tvResultPlayerName.text = getString(R.string.result_congrats_format, playerName)
        tvResultQuizTitle.text = getString(R.string.result_quiz_topic_format, quizTitle)
        tvScoreNumber.text = getString(R.string.result_score_format, score, total)
        tvPercentage.text = getString(R.string.result_percentage_format, percentage)

        tvVerdict.text = when {
            percentage == 100 -> getString(R.string.verdict_perfect)
            percentage >= 70 -> getString(R.string.verdict_good)
            percentage >= 40 -> getString(R.string.verdict_average)
            else -> getString(R.string.verdict_low)
        }

        btnPlayAgain.setOnClickListener {
            Log.i(TAG, "Нажата кнопка 'Пройти снова'")
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            finish()
        }

        btnHome.setOnClickListener {
            Log.i(TAG, "Нажата кнопка 'В главное меню'")
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            finish()
        }
    }

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
