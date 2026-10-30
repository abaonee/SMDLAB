# SideEffectsLab & Theory Implementation Plan

This implementation plan covers both **Part A (Theoretical Questions & Code Analysis)** and **Part B (Practical Assignments: SideEffectsLab app with 6 screens / tasks)** based on Lecture 13 of Jetpack Compose Side-effects.

## User Review Required

> [!IMPORTANT]
> - **Part A Answers:** Comprehensive, academically rigorous answers in Uzbek covering all 19 theoretical questions and code snippets from Lecture 13.
> - **Part B Implementation:** A multi-screen Jetpack Compose app (`SideEffectsLab`) with a main navigation hub allowing users to navigate between all 6 practical tasks, plus a root `README.md` and full documentation.

## Open Questions

- None. All requirements are clearly specified in the assignment prompt.

## Proposed Changes

### [Documentation & Theory]
- Create `PART_A_ANSWERS.md` containing detailed answers to all 19 theoretical questions (Side-effects, table of 8 APIs, PulseEffect, LaunchedEffect(true), callback coroutines, rememberUpdatedState, DisposableEffect & cleanup, MiniDisposable model, SideEffect, produceState, myProduceState implementation, derivedStateOf vs direct reading, waveform scenarios, snapshotFlow, effect key models, lifecycle observer management, code review cleanup, and comprehensive API comparison table with real project examples).

### [App Structure & UI]
- Update `app/build.gradle.kts` to include `androidx.lifecycle:lifecycle-runtime-compose`.
- Create a clean navigation / tab / screen structure in `com.example.practise9`:
  - `MainActivity.kt`: Main Hub / Navigation between the 6 practical tasks and Theory overview.
  - `tasks/Task1Splash.kt`: Splash screen with `LaunchedEffect` and `rememberUpdatedState` with parent version counter.
  - `tasks/Task2Snackbar.kt`: Scaffold + SnackbarHost + button using `rememberCoroutineScope`.
  - `tasks/Task3Lifecycle.kt`: Lifecycle logger with `DisposableEffect`, `LifecycleEventObserver`, `onDispose`, and Switch toggle.
  - `tasks/Task4Analytics.kt`: `SideEffect` syncing `Analytics.userType` and logging invocation count.
  - `tasks/Task5Scroll.kt`: `LazyColumn` (100 items), "Back to top" button comparing direct reading vs `derivedStateOf` recomposition counts, and `snapshotFlow` analytics event stream.
  - `tasks/Task6ProduceState.kt`: Bonus `produceState` implementation (`loadNetworkImage`) with Loading/Success/Error handling and repository delay.
- Create `README.md` at the project root documenting each effect API and why it was chosen.

## Verification Plan

### Automated Tests
- Run `./gradlew test` and `./gradlew assembleDebug` to ensure compilation and build success.

### Manual Verification
- Deploy app to emulator/device.
- Verify each task screen works as required:
  - Task 1: Logcat shows updated version (v3, etc.) with `rememberUpdatedState`.
  - Task 2: Snackbar launches correctly from callback via `rememberCoroutineScope`.
  - Task 3: Switch toggle adds/removes observer with matching add/remove count in Logcat.
  - Task 4: `SideEffect` increments counter and updates analytics on recomposition.
  - Task 5: Scroll LazyColumn shows difference in recomposition counts between direct reading and `derivedStateOf`; snapshotFlow logs analytics only on threshold crossing.
  - Task 6: ProduceState correctly displays Loading -> Success/Error and cancels on exit.
