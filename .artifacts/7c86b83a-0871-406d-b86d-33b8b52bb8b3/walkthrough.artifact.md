# Walkthrough - SideEffectsLab & Theory (13-Ma'ruza)

Completed all theoretical questions and practical tasks for Lecture 13 (Jetpack Compose Side-effects).

## Changes Made

### 1. Part A: Theoretical Questions & Code Analysis
- Created [PART_A_ANSWERS.md](file:///C:/Users/Lab/AndroidStudioProjects/practise9/PART_A_ANSWERS.md) with detailed, academically rigorous answers in Uzbek for all 19 theoretical questions and code snippets:
  - Definition of Side-effects and why Composable body is unsafe.
  - Complete mapping table of all 8 Effect APIs.
  - Analysis of `PulseEffect.kt`, `LaunchedEffect(true)` vs dynamic keys.
  - Callback coroutine handling via `rememberCoroutineScope`.
  - Preventing stale lambdas with `rememberUpdatedState`.
  - Resource cleanup and memory leak prevention with `DisposableEffect` and `onDispose`.
  - Simulation of `MiniDisposable` model and console output steps.
  - Safe state transmission to external objects using `SideEffect`.
  - Converting suspend sources to Compose State via `produceState` and implementing `myProduceState`.
  - Optimizing recompositions with `derivedStateOf` vs direct reading.
  - Analyzing waveform scenarios and calculation of recompositions.
  - Converting State to Flow via `snapshotFlow` with `map`, `distinctUntilChanged`, and `filter`.
  - Effect key restart models and lifecycle observer management.
  - Code review bug fixes for `ChatScreen.kt`.
  - Comprehensive summary table with real project usage examples.

### 2. Part B: Practical Assignments (SideEffectsLab App)
- Updated [app/build.gradle.kts](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/build.gradle.kts) (`compileSdk = 37`, added `androidx.lifecycle:lifecycle-runtime-compose`).
- Created `com.example.practise9.tasks` package with 6 task screens:
  - **Task 1 ([Task1Splash.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/tasks/Task1Splash.kt)):** Splash screen demonstrating `LaunchedEffect` and `rememberUpdatedState` with parent version counter updating every 500ms.
  - **Task 2 ([Task2Snackbar.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/tasks/Task2Snackbar.kt)):** Snackbar button using `rememberCoroutineScope` for event callbacks.
  - **Task 3 ([Task3Lifecycle.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/tasks/Task3Lifecycle.kt)):** Lifecycle logger using `DisposableEffect`, `LifecycleEventObserver`, and `onDispose` with toggle switch.
  - **Task 4 ([Task4Analytics.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/tasks/Task4Analytics.kt)):** `SideEffect` safely updating external `DummyAnalytics.userType` and logging invocation count.
  - **Task 5 ([Task5Scroll.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/tasks/Task5Scroll.kt)):** 100-item `LazyColumn` comparing direct reading vs `derivedStateOf` recomposition counts, plus `snapshotFlow` analytics event stream.
  - **Task 6 ([Task6ProduceState.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/tasks/Task6ProduceState.kt)):** Bonus `produceState` implementation (`loadNetworkImage`) handling Loading, Success, Error states and cancellation.
- Created [MainActivity.kt](file:///C:/Users/Lab/AndroidStudioProjects/practise9/app/src/main/java/com/example/practise9/MainActivity.kt) as a central navigation hub for testing all tasks.
- Created [README.md](file:///C:/Users/Lab/AndroidStudioProjects/practise9/README.md) documenting each effect API choice and rationale.

## Verification Results

### Automated Verification
- Ran `./gradlew assembleDebug` successfully:
```
BUILD SUCCESSFUL in 4s
```
- All classes compile correctly without errors.
