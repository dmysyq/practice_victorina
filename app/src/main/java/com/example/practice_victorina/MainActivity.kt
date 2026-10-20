package com.example.practice_victorina

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Стартовый экран приложения (MainActivity):
 * - Ввод имени участника
 * - Выбор одной из 5 доступных тем викторины
 * - Переход на экран вопросов с передачей параметров через явный Intent
 * - Логирование жизненного цикла и действий пользователя
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"

        // Ключи для явного Intent
        const val EXTRA_PLAYER_NAME = "EXTRA_PLAYER_NAME"
        const val EXTRA_QUIZ_FILE = "EXTRA_QUIZ_FILE"
        const val EXTRA_QUIZ_TITLE = "EXTRA_QUIZ_TITLE"
    }

    private lateinit var etPlayerName: EditText
    private lateinit var spinnerQuizTopic: Spinner
    private lateinit var btnStartQuiz: Button
    private lateinit var btnClearName: Button

    // Список пара (Название, имя файла JSON в assets/quizzes)
    private val quizOptions = listOf(
        Pair("Космос и планеты (10 вопросов)", "quiz_space.json"),
        Pair("Фильм: Интерстеллар (8 вопросов)", "quiz_interstellar.json"),
        Pair("Фильм: Звёздные войны (10 вопросов)", "quiz_star_wars.json"),
        Pair("Язык Kotlin (10 вопросов)", "quiz_kotlin.json"),
        Pair("Столицы государств (15 вопросов)", "quiz_capitals.json")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: экран создан")

        etPlayerName = findViewById(R.id.etPlayerName)
        spinnerQuizTopic = findViewById(R.id.spinnerQuizTopic)
        btnStartQuiz = findViewById(R.id.btnStartQuiz)
        btnClearName = findViewById(R.id.btnClearName)

        setupSpinner()
        setupListeners()
    }

    private fun setupSpinner() {
        val titles = quizOptions.map { it.first }
        val adapter = ArrayAdapter(this, R.layout.item_spinner_selected, titles)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerQuizTopic.adapter = adapter
    }

    private fun setupListeners() {
        btnStartQuiz.setOnClickListener {
            val name = etPlayerName.text.toString().trim()

            if (name.isEmpty()) {
                Log.w(TAG, "Поле имени пустое, старт отклонён")
                Toast.makeText(this, getString(R.string.msg_empty_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedIndex = spinnerQuizTopic.selectedItemPosition
            val selectedQuiz = quizOptions.getOrElse(selectedIndex) { quizOptions[0] }

            Log.i(TAG, "Старт викторины: игрок '$name', тема '${selectedQuiz.first}'")

            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra(EXTRA_PLAYER_NAME, name)
                putExtra(EXTRA_QUIZ_FILE, selectedQuiz.second)
                putExtra(EXTRA_QUIZ_TITLE, selectedQuiz.first)
            }
            startActivity(intent)
        }

        btnClearName.setOnClickListener {
            Log.i(TAG, "Поле ввода имени очищено")
            etPlayerName.text.clear()
        }
    }

    // Методы жизненного цикла с кратким системным логированием
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