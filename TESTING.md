# Учебные тесты

Тесты построены по примеру MoviesDemo преподавателя. Проверяем три уровня
отдельно: бизнес-логику, управление состоянием и интерфейс одного экрана.
Рабочий код приложения для этого не менялся.

## С чего начать читать

1. `app/src/test/java/com/example/pokeapplication/domain/usecase/FakePokemonRepository.kt`.
   Это реализация нашего Repository с данными в памяти. `MutableStateFlow`
   уведомляет подписчиков об изменениях, как это делает Room. Настоящих запросов нет.
2. `GetPokemonListUseCaseTest.kt` в той же папке — 7 тестов поиска, фильтров и сортировки.
3. `app/src/test/java/com/example/pokeapplication/MainDispatcherRule.kt`.
   Правило подменяет Android Main-поток тестовым диспетчером и возвращает его после теста.
4. `app/src/test/java/com/example/pokeapplication/presentation/viewmodel/PokemonListViewModelTest.kt`.
   8 тестов настоящей ViewModel с настоящими Use Case и FakeRepository.
5. `app/src/androidTest/java/com/example/pokeapplication/pokemonlist/HomeScreenTest.kt`.
   7 тестов HomeContent: карточки, поиск, фильтры, загрузка, сообщения и нижняя панель.

## Как устроен каждый тест

- Подготовка: задаём исходные данные и создаём проверяемый объект.
- Действие: вызываем метод или нажимаем элемент интерфейса.
- Проверка: `assertEquals`, `assertTrue` и другие проверки сравнивают результат с ожидаемым.

`@Test` обозначает отдельный тест, `@Before` выполняется перед каждым тестом,
`@After` — после него, `@Rule` подключает правило JUnit.
Каждый тест получает собственный FakeRepository, поэтому не зависит от других тестов.

## Корутины без настоящего ожидания

`runTest` запускает тест корутин с виртуальным временем.
`runCurrent()` выполняет готовые задачи, но не пропускает задержки.
`advanceUntilIdle()` выполняет запланированные задачи, включая виртуальные задержки.
Поэтому тест загрузки с `delay(1000)` не обязан ждать настоящую секунду.

Turbine (`uiState.test`) позволяет читать значения Flow и проверять переход
из загрузки в готовое состояние. В остальных тестах достаточно текущего `uiState.value`.
После теста отменяем `viewModelScope`, чтобы закончить непрерывное наблюдение Flow.

## Границы UI-тестов

Как у преподавателя, UI получает готовый UiState и обработчики событий.
Мы проверяем, что экран отображает состояние и передаёт правильные аргументы обработчикам.
Изменение настоящих данных проверяется отдельно на уровне ViewModel и Use Case.
UI-тесты не проверяют сеть, Supabase, Room, реальные переходы навигации или загрузку изображений.

## Запуск

В Android Studio нажми зелёную кнопку возле тестового класса или метода.
Тесты из `src/test` запускаются на компьютере, из `src/androidTest` — на устройстве/эмуляторе.

Из терминала Windows в корне проекта:

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.pokeapplication.pokemonlist.HomeScreenTest
```

HTML-отчёты Gradle:

- Unit: `app/build/reports/tests/testDebugUnitTest/index.html`.
- UI: `app/build/reports/androidTests/connected/debug/index.html`.

В проекте также остаются два исходных шаблонных Example-теста Android Studio;
они не входят в 22 новых учебных теста.
