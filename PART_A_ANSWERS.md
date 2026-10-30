# A qism. Nazariy savollar va kod bilan ishlash (13-Ma'ruza)

## 1-savol
* **Side-effect ta'rifi:** Side-effect (yon ta'sir) — bu Composable funksiyaning tashqi dunyoga (composable doirasidan tashqari holatlarga) ta'sir qiluvchi yoki ma'lumot o'qib-yozuvchi jarayoni (masalan, tarmoqqa so'rov yuborish, ma'lumotlar bazasiga yozish, analytics'ga log yozish, tashqi obyektlar yoki callback'larni yangilash).
* **Composable tanasi nima uchun xavfli:** Composable tanasi (body) har qanday vaqtda, qayta-qayta (recomposition paytida o'nlab marta) chaqirilishi mumkin. Agar uning ichida to'g'ridan-to'g'ri `viewModel.loadProfile()` yoki `analytics.log()` yozilsa, har safar ekran animatsiyasi yoki holat o'zgarganda so'rovlar qayta-qayta takrorlanib, xotira oqishi, server yuklamasi va cheksiz sikllarga olib keladi. Recomposition sharoitga ko'ra bekor qilinishi (discard) yoki parallel ravishda qayta boshlanishi mumkin.
* **Effect ta'rifi (Key Term):** Effect — bu Composition doirasidan tashqarida ishlaydigan va Compose sikliga (lifecycle) bog'langan suspend yoki imperativ kod bloki.
* **Unidirectional Data Flow (Bir tomonlama ma'lumot oqimi):** Ma'lumot yuklash va biznes mantiq Composable UI'da emas, balki `ViewModel`da amalga oshirilishi shart. Composable faqat holatni (State) ko'rsatadi va foydalanuvchi harakatlarini voqea (Event) sifatida ViewModel'ga uzatadi.

---

## 2-savol
Sakkizta effect API va ularning ehtiyojlarga mos kelishi:

| Men xohlayman | API |
| :--- | :--- |
| Composable Composition'da turganda suspend funksiyani chaqirish | `LaunchedEffect` |
| onClick kabi callback ichidan coroutine ishga tushirish | `rememberCoroutineScope` |
| Effect ichida eng so'nggi qiymatni o'qish, lekin effect'ni qayta ishga tushirmaslik | `rememberUpdatedState` |
| Listener qo'shish va keyin uni olib tashlash | `DisposableEffect` |
| Compose state'ni Compose boshqarmaydigan kodga uzatish | `SideEffect` |
| Flow, LiveData yoki RxJava'ni Compose State'ga aylantirish | `produceState` |
| Kirish qiymatidan kamroq recompose qilish | `derivedStateOf` |
| Compose State'ni Flow'ga aylantirish | `snapshotFlow` |

---

## 3-savol
* **(1) Effect qachon ishga tushadi?** `LaunchedEffect(pulseRateMs)` Composition'ga kirganda ishga tushadi va kalit (`pulseRateMs`) o'zgarganda eski coroutine bekor qilinib, yangisi boshlanadi.
* **(2) pulseRateMs 3000 dan 1000 ga o'zgarganda nima bo'ladi?** Joriy ishlayotgan delay/animation coroutine darhol to'xtatiladi (cancel) va yangi `pulseRateMs = 1000` qiymati bilan sikl noldan boshlanadi.
* **(3) Composable Composition'ni tark etganda:** while sikli va coroutine avtomatik ravishda bekor qilinadi. `isActive` ko'rsatkichi coroutine hali ham faol/bekor qilinmaganligini tekshiradi. Ma'ruzadagi MiniComposition modeli `effect(100) cancelled` orqali eski effect tugatilib, resurslar tozalanishini ko'rsatadi.

---

## 4-savol
* **Nega LaunchedEffect(true) “while(true) kabi shubhali”:** `true` doimiy kalit bo'lgani uchun, qiymat hech qachon o'zgarmaydi va effect hech qachon restart qilmaydi. Agar ichida o'zgaruvchi (`userId`) ishlatilsa, effect eski `userId` qiymatini yopib qo'yadi (capture qiladi) va `userId` o'zgarganda so'rov eski ID bilan ketaveradi.
* **Qaysi holatda doimiy kalit o'rinli:** Faqatgina Composition'ga kirganda bir martagina bajarilishi kerak bo'lgan vaziyatlarda (masalan, ma'ruzadagi Splash ekrani vaqt o'lchovi yoki bir martalik analytics voqeasi).
* **Tuzatilgan kod:**
```kotlin
LaunchedEffect(userId) {
    api.sendHello(userId)
}
```

---

## 5-savol
* **Nega LaunchedEffect callback ichidan chaqirib bo'lmaydi:** `LaunchedEffect` bu Composable funksiya bo'lib, uni faqat Composition ichida (Composable tanasida) chaqirish mumkin. `onClick` — bu oddiy lambda callback, u Composition doirasidan tashqarida, tugma bosilganda ishlaydi.
* **rememberCoroutineScope:** Composition doirasida coroutine'larni ishga tushirish uchun maxsus coroutine scope qaytaradi. Uning umri composable Composition'da turgan muddatga bog'liq; composable Composition'ni tark etganda scope avtomatik ravishda cancel qilinadi.
* **To'g'ri variant:**
```kotlin
val scope = rememberCoroutineScope()
Button(onClick = {
    scope.launch {
        snackbarHostState.showSnackbar("Hi")
    }
}) { Text("Hi") }
```
* **showSnackbar suspend funksiya ekani:** Snackbarni ko'rsatish va uning tugashini kutish (delay) asinxron amal bo'lgani uchun uni coroutine ichida chaqirish talab etiladi.
* **Qoida:** “Composable paydo bo'lgani uchun boshlanadigan ish” -> `LaunchedEffect`; “Foydalanuvchi biror narsa qilgani uchun (event) boshlanadigan ish” -> `rememberCoroutineScope` orqali scope.launch.

---

## 6-savol
* **Nega effect eski lambdani chaqirib yuborishi mumkin:** `LaunchedEffect(true)` ichida ishlatilgan `onTimeout` dastlabki yaratilgan lambda'ni yopib qo'yadi (captured). Ota composable recompose bo'lib yangi lambda bersa ham, LaunchedEffect eski lambdani chaqiradi.
* **rememberUpdatedState buni qanday hal qiladi:** `val currentOnTimeout by rememberUpdatedState(onTimeout)` har doim lambda'ning eng so'nggi (updated) qiymatini xotirada saqlaydi, lekin effect kalitini o'zgartirmaydi.
* **Nega onTimeout ni effect kaliti qilish noto'g'ri:** Agar `onTimeout` kalit qilinsa, har safar ota recompose bo'lib yangi lambda berganda coroutine to'xtatilib, kutish (delay) noldan boshlanadi, bu esa splash vaqtini buzadi.
* **Model chiqishi:** `captured -> onTimeout v1` eski lambda ishlatilganini, `updatedState -> onTimeout v3` esa `rememberUpdatedState` har doim eng so'nggi versiyani chaqirganini isbotlaydi.

---

## 7-savol
* **Nega tozalash (cleanup) kerak va xotira oqishi:** Listener (observer) qo'shilgandan so'ng, agar u olib tashlanmasa (`removeObserver`), Lifecycle egasi (owner) xotirada qolib ketadi, bu esa xotira oqishiga (Memory Leak) va ortiqcha callback chaqiruvlariga olib keladi.
* **onDispose qoidasi:** `DisposableEffect` blokining oxirgi qatori doim `onDispose { ... }` bo'lishi shart. Agar u yozilmasa, kompilyatsiya xatosi yuz beradi.
* **Bo'sh onDispose nega yomon amaliyot:** Agar tozalash kerak bo'lgan resurs bo'la turib bo'sh onDispose yozilsa, resurs tozalanmaydi. Agar umuman tozalash talab qilinmasa, `LaunchedEffect` mosroq keladi.
* **Tuzatilgan kod:**
```kotlin
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event -> /* ... */ }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
```

---

## 8-savol
* **Konsol chiqishi (to'rt qator):**
  1. `A.addObserver(observer)`
  2. `A.removeObserver(observer)`
  3. `B.addObserver(observer)`
  4. `A.removeObserver(observer)` (yoki composition tark etganda oxirgi owner remove bo'ladi)
  *Aniqlik uchun:*
  - `screen("A")` -> `A.addObserver(observer)`
  - ikkinchi `screen("A")` -> Kalit o'zgarmagani uchun hech narsa chiqarmaydi.
  - `screen("B")` -> Kalit "A" dan "B" ga o'zgardi. Avval eski effect to tozalanadi: `A.removeObserver(observer)`, keyin yangisi qo'shiladi: `B.addObserver(observer)`.
  - `comp.leave()` -> Composition'dan chiqqanda oxirgi faol effect tozalanadi: `B.removeObserver(observer)`.
  *Natija:*
  1. `A.addObserver(observer)`
  2. `A.removeObserver(observer)`
  3. `B.addObserver(observer)`
  4. `B.removeObserver(observer)`
* **B kelganda nima birinchi bajariladi:** Avval eski observer olib tashlanadi (`removeObserver`), keyin yangisi qo'shiladi (`addObserver`).

---

## 9-savol
* **SideEffect qachon ishga tushadi:** Har safar Composable muvaffaqiyatli recomposition'dan o'tib, UI ga o'rnatilgandan (committed) so'ng darhol ishga tushadi.
* **To'g'ridan-to'g'ri chaqirish nega xavfli:** Agar Composable recomposition paytida xatolik yoki bekor qilinish (discard) tufayli to'xtatilsa ham, to'g'ridan-to'g'ri yozilgan tashqi kod bajarilib ketib, tashqi tizimga noto'g'ri yoki takroriy ma'lumot yuborib yuboradi.
* **Attempt modeli:**
  - Direct call, commits=false: Analytics darhol chaqirilib yuboriladi (garchi UI ga chizilmagan bo'lsa ham).
  - SideEffect, commits=false: Recomposition bekor qilingani uchun SideEffect ishlamaydi (analytics chaqirilmaydi).
  - SideEffect, commits=true: Recomposition muvaffaqiyatli yakunlangach, SideEffect ishlaydi va analytics xavfsiz chaqiriladi.
* **SideEffect kalitlari:** SideEffect'ning kalitlari yo'q, chunki u har bir muvaffaqiyatli commit paytida ishlaydi.

---

## 10-savol
* **produceState:** Suspend funksiyalar yoki tashqi oqimlarni Compose `State`ga o'rab beruvchi API. Producer coroutine sifatida ishga tushadi va Composable Composition'ni tark etganda avtomatik bekor qilinadi.
* **Conflates (bir xil qiymat):** Agar producer avvalgi qiymat bilan bir xil qiymat chiqarsa (`value = newValue`), Compose recomposition chaqirmaydi (qiymat o'zgarmagani uchun tejaladi).
* **Kichik harf bilan boshlanishi:** `loadNetworkImage` bu maxsus factory/helper composable funksiya bo'lib, u `State<Result>` qaytaradi. Compose qoidalariga ko'ra, qiymat qaytaruvchi va holat boshqaruvchi yordamchi composable funksiyalar kichik harf bilan boshlanishi tavsiya etiladi.

---

## 11-savol
* **produceState ning ichki tuzilishi (myProduceState):**
```kotlin
@Composable
fun <T> myProduceState(
    initialValue: T,
    key: Any?,
    producer: suspend MutableState<T>.() -> Unit
): State<T> {
    val state = remember { mutableStateOf(initialValue) }
    LaunchedEffect(key) {
        state.producer()
    }
    return state
}
```
* **Xatti-harakati va kalit o'zgarganda:** Bu funksiya huddi `LaunchedEffect` kabi ishlaydi. Kalit o'zgarganda eski producer coroutine bekor qilinadi va yangisi boshlanadi.

---

## 12-savol
* **Nega to'g'ridan-to'g'ri o'qish keraksiz recomposition keltiradi:** LazyColumn har safar scroll bo'lganda `firstVisibleItemIndex` o'zgaradi. Agar uni to'g'ridan-to'g'ri o'qisak, butun ro'yxat va ota composable har bir scroll pikselligida qayta chiziladi (recompose bo'ladi).
* **derivedStateOf buni qanday hal qiladi:** `derivedStateOf` faqatgina natija o'zgargandagina (`> 0`dan `false`ga yoki aksincha) recomposition'ni ishga tushiradi. Indekslar o'zgarsa ham, agar `showButton` qiymati o'zgarmasa, UI recompose bo'lmaydi.
* **remember ichiga o'ralishi:** `derivedStateOf` obyektining har bir recomposition'da qaytadan yaratilishining oldini olish uchun `remember` bilan xotirada saqlanadi.
* **Hisob-kitob:** Indekslar `[0, 0, 1, 2, 3, 4, 5, 3, 1, 0]`:
  - To'g'ridan-to'g'ri o'qish: har bir o'zgarishda recomposition -> 9 ta o'zgarish = 9 ta recomposition.
  - derivedStateOf bilan: `showButton` faqat 0 -> 0 (false), 0 -> 1 (true, o'zgardi!), 1 -> 2 (true), ..., 1 -> 0 (false, o'zgardi!). Jami faqat 3 marta qiymat o'zgaradi (`false -> true`, `true -> false`) -> 3 ta recomposition.

---

## 13-savol
* **Nega bu holatda derivedStateOf ortiqcha xarajat:** `firstName` va `lastName` o'zgarganda string interpolation (`"$firstName $lastName"`) har doim yangi natija qaytaradi. Natija har safar o'zgargani uchun `derivedStateOf` filtrlay olmaydi, aksincha qo'shimcha obyekt yaratish xarajatini keltirib chiqaradi.
* **To'g'ri variant:**
```kotlin
val fullName = "$firstName $lastName"
```
* **Qo'llash mezoni:** Kirish ma'lumotlari tez-tez o'zgarib, lekin chiqish (natija) juda kamdan-kam holatda o'zgarganda ishlatiladi.
* **Caution (Ogohlantirish):** `derivedStateOf` ni oddiy o'zgaruvchilarni birlashtirish yoki string format qilish uchun ishlatmang; bu faqat State o'qish chastotasini kamaytirish (throttling/debouncing) kerak bo'lganda qo'llaniladi.

---

## 14-savol
* **derivedStateOf qachon ishlatiladi:** UI holati juda tez-tez o'zgarganda (masalan, scroll indeksi, vaqt o'lchovi, sensor ma'lumotlari) va UI elementi faqat muayyan shart bajarilgandagina yangilanishi kerak bo'lganda.
* **Qaysi 3 holatda ishlatilmaydi:**
  1. *Amplitudalar oldindan hisoblangan:* Agar ma'lumotlar allaqachon tayyor va filtrdan o'tgan bo'lsa.
  2. *Waveform statik:* O'zgarmaydigan ma'lumotlar uchun state kuzatish shart emas.
  3. *Waveform LaunchedEffect yoki animate* bilan qo'lda yangilansa:* Bu yerda animatsiya o'z state mexanizmiga ega, derivedStateOf kerak emas.

---

## 15-savol
* **snapshotFlow:** Compose State o'qishlarini Kotlin `Flow`ga aylantiruvchi funksiya. Cold Flow hisoblanadi, chunki u faqat collector ulangandagina ishlay boshlaydi va state o'zgarganda yangi qiymat chiqaradi.
* **Operatorlar vazifasi:**
  - `map`: indeksni boolean (`index > 0`) qiymatga o'tkazadi.
  - `distinctUntilChanged`: ketma-ket bir xil qiymatlar kelganda takrorlanishning oldini oladi (faqat qiymat o'zgandagina o'tkazadi).
  - `filter`: faqat `it == true` bo'lgandagina o'tkazadi.
* **LaunchedEffect ichida:** Flow'larni yig'ish (collect) suspend funksiya bo'lgani uchun uni `LaunchedEffect` coroutine ichida bajarish talab etiladi.
* **Hisob-kitob (0, 0, 1, 2, 5, 3, 0, 0, 1, 4):**
  - Oqim:
    1. `0` -> `index > 0` = `false`
    2. `0` -> `false` (distinct: o'zgarmadi)
    3. `1` -> `true` (o'zgardi! 1-hodisa yuboriladi)
    4. `2` -> `true` (distinct: o'zgarmadi)
    5. `5` -> `true` (distinct: o'zgarmadi)
    6. `3` -> `true` (distinct: o'zgarmadi)
    7. `0` -> `false` (o'zgardi, lekin filter `true`ni qabul qiladi -> yuborilmaydi)
    8. `0` -> `false` (distinct: o'zgarmadi)
    9. `1` -> `true` (o'zgardi! 2-hodisa yuboriladi)
    10. `4` -> `true` (distinct: o'zgarmadi)
  - **Jami yuborilgan hodisalar soni:** 2 ta.

---

## 16-savol
* **Jadval:**

| Kalitlar | Restart soni (bashorat) | Sababi |
| :--- | :--- | :--- |
| `keys = []` | 0 (yoki 1 marta boshida) | Kalit bo'lmagani uchun effect hech qachon restart qilmaydi, lekin eskirgan qiymat bilan ishlashi mumkin (xato). |
| `keys = [owner]` | 1 marta (owner o'zgarganda, ya'ni 3-tickda) | Faqat owner o'zgarganda restart qiladi. |
| `keys = [owner, tick]` | Har bir tick o'zgarganda (4 marta) | Har safar tick yoki owner o'zgarganda effect qayta ishga tushadi (samarasiz). |

---

## 17-savol
* **Qaysi o'zgaruvchilar kalit bo'lishi kerak:** `lifecycleOwner`.
* **Nega currentOnStart va currentOnStop kalit emas:** Chunki ular `rememberUpdatedState` bilan o'ralgan; ularning maqsadi effect ichida eng so'nggi callback'ni chaqirish, lekin ularning o'zgarishi effect'ni qaytadan boshlashiga (restart) ehtiyoj yo'q.
* **lifecycleOwner kalit qilinmasa:** Agar lifecycleOwner o'zgarganda effect restart qilmasa, observer eski lifecycle owner'da qolib ketib, xotira oqishi yoki xatolikka olib keladi.
* **Key Point qoidasi:** Effect kalitlari sifatida faqat effect ichida ishlatiladigan va o'zgarganda effect'ni noldan boshlash talab etiladigan o'zgaruvchilar (dependency) ko'rsatilishi kerak. Callback'lar esa `rememberUpdatedState` orqali boshqariladi.

---

## 18-savol
* **To'rtta xato va ularning sabablari:**
  1. *(1) `viewModel.markOpened(userId)` to'g'ridan-to'g'ri composable tanasida chaqirilgan.* Sababi: Recomposition paytida bu so'rov qayta-qayta takrorlanib ketadi. Buni `LaunchedEffect(userId)` ichiga olish kerak.
  2. *(2) `LaunchedEffect(true)` doimiy kalit bilan yozilgan.* Sababi: `userId` o'zgarganda ham eskirgan user uchun ishlaydi yoki qayta ishlamaydi. Kalitga `userId` berilishi kerak.
  3. *(3) `DisposableEffect(owner)` ichida `onDispose` yozilmagan.* Sababi: Kompilyatsiya xatosi beradi va observer tozalanmaydi. `onDispose { owner.lifecycle.removeObserver(observer) }` qo'shilishi shart.
  4. *(4) `Button(onClick = { LaunchedEffect(Unit) { send() } })` callback ichida LaunchedEffect chaqirilgan.* Sababi: `LaunchedEffect` callback ichida ishlatilmaydi. Uning o'rniga `rememberCoroutineScope` ishlatilib, `scope.launch { send() }` chaqirilishi kerak.

* **Tuzatilgan variant:**
```kotlin
@Composable
fun ChatScreen(userId: String, onClose: () -> Unit) {
    val scope = rememberCoroutineScope()
    val owner = LocalLifecycleOwner.current
    val observer = remember { LifecycleEventObserver { _, _ -> } }
 
    LaunchedEffect(userId) {
        viewModel.markOpened(userId)
    }
    
    LaunchedEffect(userId) {
        delay(5_000)
        onClose()
    }
    
    DisposableEffect(owner) {
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
    
    Button(onClick = {
        scope.launch { send() }
    }) { Text("Send") }
}
```

---

## 19-savol
* **Yakuniy jadval:**

| API | Qachon ishlaydi | Tozalash | Restart sababi | Real foydalanish misoli |
| :--- | :--- | :--- | :--- | :--- |
| `LaunchedEffect` | Composition'ga kirganda | Coroutine cancel bo'ladi | Kalit o'zgaganda | Tarmoqdan profil ma'lumotini yuklash |
| `rememberCoroutineScope` | Callback ichida chaqirilganda | Scope composition tark etganda cancel bo'ladi | Mavjud emas | Tugma bosilganda Snackbar ko'rsatish |
| `rememberUpdatedState` | Recomposition paytida darhol yangilanadi | Mavjud emas | Mavjud emas | Splash ekranda eng so'nggi callback'ni ushlash |
| `DisposableEffect` | Composition'ga kirganda | `onDispose` bloki ishlaydi | Kalit o'zgaganda | Lifecycle observer qo'shish va olish |
| `SideEffect` | Har bir muvaffaqiyatli commit'dan so'ng | Mavjud emas | Har recomposition'dan keyin | Analytics kutubxonasiga holatni uzatish |
| `produceState` | Composition'ga kirganda producer boshlanadi | Producer coroutine cancel bo'ladi | Kalit o'zgaganda | Tarmoqdan rasm yoki ma'lumot yuklab State qaytarish |
| `derivedStateOf` | State o'qilganda hisoblanadi | Mavjud emas | Ichidagi State'lar o'zgaganda | LazyColumn scroll indeksiga qarab tugmani ko'rsatish |
| `snapshotFlow` | Collector ulanganda boshlanadi | Flow collect to'xtaganda | Mavjud emas | Scroll indeksini Analytics'ga Flow orqali yuborish |
