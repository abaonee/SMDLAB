# SideEffectsLab — 13-Ma'ruza: Jetpack Compose Side-effects

Bu loyiha Jetpack Compose'dagi **Side-effects** (yon ta'sirlar) bo'yicha 13-ma'ruza talablari asosida ishlab chiqilgan bo'lib, ikkita qismdan iborat:
1. **A qism:** 19 ta nazariy savollar va kod tahlillari (`PART_A_ANSWERS.md` faylida to'liq yoritilgan).
2. **B qism:** Android Studio'da bajarilgan 6 ta amaliy topshiriq (`SideEffectsLab` ilovasi).

---

## Amaliy Topshiriqlar va Tanlangan Effect API'lar

### 1-topshiriq. Splash ekrani
* **Tanlangan API:** `LaunchedEffect` va `rememberUpdatedState`
* **Nega tanlandi:** 
  - `LaunchedEffect(true)` — ekran ochilganda 2 soniya kutish (delay) asinxron amalini bajarish uchun.
  - `rememberUpdatedState` — ota composable'dan keladigan `onTimeout` lambda'sining eng so'nggi versiyasini (eski versiyani capture qilib qolmasdan) ta'minlash uchun, bunda kutish vaqtini qaytadan boshlab yubormaslikka erishiladi.

### 2-topshiriq. Snackbar tugmasi
* **Tanlangan API:** `rememberCoroutineScope`
* **Nega tanlandi:**
  - Tugma bosilishi (`onClick`) bu callback bo'lib, u Composition doirasidan tashqarida sodir bo'ladi. `LaunchedEffect`ni callback ichidan chaqirib bo'lmaydi.
  - `rememberCoroutineScope` callback ichidan coroutine ishga tushirishga imkon beradi va composable Composition'ni tark etganda coroutine'ni avtomatik bekor qilib xotira oqishining oldini oladi.

### 3-topshiriq. Lifecycle logger
* **Tanlangan API:** `DisposableEffect`
* **Nega tanlandi:**
  - Lifecycle listener'larni (`LifecycleEventObserver`) qo'shish va composable yo'qolganda ularni albatta olib tashlash (`removeObserver`) talab etiladi.
  - `DisposableEffect` o'zining majburiy `onDispose` bloki tufayli resurslarni tozalashni kafolatlaydi.

### 4-topshiriq. SideEffect va analytics
* **Tanlangan API:** `SideEffect`
* **Nega tanlandi:**
  - Compose holatini (`userType`) Compose boshqarmaydigan tashqi obyektga (`Analytics`) xavfsiz tarzda faqat muvaffaqiyatli recomposition commit'dan keyin yetkazish uchun.
  - To'g'ridan-to'g'ri composable tanasida yozish bekor qilingan (discard) recomposition'larda ham tashqi obyektga ta'sir qilib yuborishi mumkin.

### 5-topshiriq. Scroll: derivedStateOf va snapshotFlow
* **Tanlangan API:** `derivedStateOf` va `snapshotFlow`
* **Nega tanlandi:**
  - `derivedStateOf` — scroll indeksining har bir o'zgarishida emas, balki faqat tugma ko'rinishi sharti (`> 0`) o'zgandagina recomposition chaqirib, unumdorlikni keskin oshiradi.
  - `snapshotFlow` — Compose State o'qishlarini Kotlin `Flow` ga o'tkazib, `map`, `distinctUntilChanged`, `filter` operatorlari yordamida chegaradan o'tganda yagona analytics hodisasini yuborish uchun.

### 6-topshiriq. Bonus: produceState
* **Tanlangan API:** `produceState`
* **Nega tanlandi:**
  - Suspend funksiya (`fakeLoadNetworkImage`) natijasini Compose `State<ImageState>` ga o'rash uchun.
  - URL o'zgarganda producer avtomatik ravishda eski so'rovni bekor qilib, yangisini boshlaydi va composable chiqib ketganda resurslarni bo'shatadi.

---

## Loyihani ishga tushirish
1. Loyihani Android Studio'da oching.
2. Gradle sync'ni bajaring.
3. Emulator yoki real qurilmada `Run` tugmasini bosing.
4. Logcat'da tegishli tag'lar (`Task1Splash`, `Task3Lifecycle`, `Task4Analytics`, `Task5Analytics`, `Task6ProduceState`) bo'yicha natijalarni kuzating.
