
## 📱 Состав экранов (Activity)
Приложение состоит из 3-х связанных между собой экранов:

1. **`MainActivity` (Стартовый экран)**
   - Разметка на базе **`LinearLayout`** (вертикальный стек + вложенный горизонтальный ряд кнопок).
   - Ввод имени игрока с валидацией (`Log.w`).
   - Выбор темы викторины через `Spinner`.
   - Кнопки «Начать викторину» и «Очистить».
   - Переход на экран вопросов через **явный Intent** с передачей данных через `putExtra()`.

2. **`QuizActivity` (Игровой раунд)**
   - Разметка на базе **`LinearLayout`**.
   - Чтение выбранной викторины из JSON в каталоге `assets/quizzes/`.
   - Отображение текущего вопроса, индикатора прогресса (`ProgressBar`) и 4 вариантов ответов.
   - Динамическая интерактивная проверка ответа (зеленая подсветка правильного, красная — ошибки).
   - Подсчет баллов и переход на экран результатов через **явный Intent**.

3. **`ResultActivity` (Итоги викторины)**
   - Разметка на базе **`RelativeLayout`** с привязками к краям (`alignParentBottom`, `alignParentTop`) и между элементами (`toEndOf`, `layout_below`).
   - Отображение имени игрока, темы, количества верных ответов (например, `10 / 10`), процента и мотивирующего вердикта.
   - Кнопки «Пройти снова» и «В главное меню».

---

## 📂 Структура проекта
```
practice_victorina/
├── app/
│   ├── build.gradle.kts                # Настройки сборки, SDK (minSdk 26, targetSdk 37)
│   └── src/main/
│       ├── AndroidManifest.xml         # Объявление всех 3 Activity и стартового экрана
│       ├── assets/quizzes/             # Файлы викторин в формате JSON
│       │   ├── quiz_space.json         # 10 вопросов по космосу
│       │   ├── quiz_interstellar.json  # 8 вопросов по фильму Интерстеллар
│       │   ├── quiz_star_wars.json     # 10 вопросов по Звездным войнам
│       │   ├── quiz_kotlin.json        # 10 вопросов по языку Kotlin
│       │   └── quiz_capitals.json      # 15 вопросов по столицам
│       ├── java/com/example/practice_victorina/
│       │   ├── MainActivity.kt         # Стартовый экран
│       │   ├── QuizActivity.kt         # Экран вопросов
│       │   ├── ResultActivity.kt       # Экран результатов
│       │   └── QuizModel.kt            # Модель данных Question
│       └── res/
│           ├── drawable/               # Селекторы кнопок, фоны карточек, векторные иконки
│           ├── layout/
│           │   ├── activity_main.xml   # Разметка LinearLayout
│           │   ├── activity_quiz.xml   # Разметка раунда вопросов
│           │   ├── activity_result.xml # Разметка RelativeLayout
│           │   ├── item_spinner_selected.xml
│           │   └── item_spinner_dropdown.xml
│           └── values/
│               ├── colors.xml          # Цвета: Ржавый, Хром, Графит
│               ├── dimens.xml          # Размеры шрифтов и отступов
│               ├── strings.xml         # Все локализованные строки
│               └── themes.xml          # Темы оформления приложения
├── docs/                               # Документация к практической работе
│   ├── 01_overview.md                  # Общее описание проекта
│   ├── 02_architecture_and_activities.md # Архитектура экранов и явные Intent
│   ├── 03_layouts_and_styling.md       # Контейнеры ViewGroup и стили
│   ├── 04_json_storage.md              # Хранение вопросов в формате JSON
│   └── 05_logging_and_evaluation.md    # Логирование и соответствие критериям (11/11)
├── logs/
│   └── logs.txt                        # Логирование Logcat по сценарию работы
└── README.md
```

---

## 🛠 Сборка и запуск

### 1. Сборка APK через Gradle:
```bash
./gradlew assembleDebug
```
Готовый установочный файл APK находится по пути:
`app/build/outputs/apk/debug/app-debug.apk`

