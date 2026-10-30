# PermissionsLab — Amaliy mashg'ulot

**Talaba:** (Talaba ismi)  
**Guruh:** (Guruh)  
**Sana:** 30.10.2026  
**Loyiha:** `uz.ttpu.permissionslab` (Android Studio — Jetpack Compose)  

---

## B qism — Amaliy topshiriqlar bo'yicha hisobot

### Sinalgan Android versiyalari va qurilmalar:
1. **API 34 (Android 14)** — Android Emulator: CAMERA, Overlay (SYSTEM_ALERT_WINDOW), One-time Location ("Only this time") va POST_NOTIFICATIONS sinovdan o'tkazildi.
2. **API 30 (Android 11)** — Android Emulator: "Denied twice" holati, Settings intent o'tishi va bir martalik ruxsatnoma bekor bo'lishi tekshirildi.
3. **API 28 (Android 9)** — POST_NOTIFICATIONS va background location SDK_INT shartlari avtomatik o'tkazilishi tasdiqlandi.

### 5-topshiriq kuzatuv natijasi ("Only this time" / Bir martalik joylashuv):
Ekranda `ACCESS_FINE_LOCATION` va `ACCESS_COARSE_LOCATION` so'ralganda **"Only this time"** (Faqat bu safar) opsiyasi tanlandi. Ilova fon rejimiga o'tkazilib, qaytarilganda, Android 11+ tizimi granted ruxsatni avtomatik ravishda bekor qildi. `LifecycleEventEffect(Lifecycle.Event.ON_RESUME)` orqali har safar ekran aktivlashganda permission holati qayta o'qilganligi sababli, UI darhol **"Joylashuv Ruxsati Yo'q"** holatiga muvaffaqiyatli o'tdi. Agar ruxsat holati `remember` ichida static keshlab qo'yilganida edi, ilova xatolik bergan bo'lardi.

---

## A qism. Nazariy savollar va javoblar

### 1-savol
**Javob:**
1. **Ruxsatnomalar bilan ishlash jarayonining (workflow) 4 ta qarori:**
   - **1-qaror:** Ruxsatnoma umuman kerakmi-yo'qmi hal qilish (Ilova ichida muqobil variant, masalan System Intent bor-yo'qligini tekshirish).
   - **2-qaror:** Ruxsatnoma ilgari berilganligini tekshirish (`checkSelfPermission`).
   - **3-qaror:** Rationale (tushuntirish) ko'rsatish kerakligini aniqlash (`shouldShowRequestPermissionRationale`).
   - **4-qaror:** Ruxsatnomani so'rash dialogini chiqarish (`requestPermissions` / Activity Result API launcher).
2. **Ruxsatnomasiz bajariladigan holatga misol:** Profilga rasm qo'yish uchun kameraga to'g'ridan-to'g'ri ruxsat so'ramay, tizimning tayyor `ActivityResultContracts.TakePicture()` yoki MediaStore Photo Picker (`PickVisualMedia`) intentidan foydalanish.
3. **Nega eng yaxshi variant:** Bu foydalanuvchidan ortiqcha ruxsat so mezonini kamaytiradi, maxfiylikni ta'minlaydi va kodingizda murakkab runtime dialog hamda xatoliklarni boshqarish mantig'ini talab qilmaydi.

---

### 2-savol
**Javob:**
1. **Ruxsatnomalarning uchta toifasi va berilish tartibi:**
   - **Install-time (O'rnatish vaqti):** Normal va Signature ruxsatnomalar. Ilova Play Store'dan o'rnatilayotganda tizim tomonidan avtomatik beriladi.
   - **Runtime (Ishlash vaqti):** Dangerous ruxsatnomalar (CAMERA, LOCATION, CONTACTS va h.k.). Ilova ishlayotgan vaqtda foydalanuvchiga tizim dialogi ko'rsatilib so'raladi.
   - **Special (Maxsus):** SYSTEM_ALERT_WINDOW, WRITE_SETTINGS va boshqalar. Tizim dialogi orqali berilmaydi; foydalanuvchi Sozlamalar (Settings) ekraniga yo'naltirilib, u yerdan qo'lda yoqilishi shart.
2. **Kod hajmini belgilash sababi:** Install-time ruxsatnomalari faqat `AndroidManifest.xml` da 1 qator e'lon qilinadi (0 qator Kotlin kodi). Runtime ruxsatnomalar holatni tekshirish, rationale ko'rsatish va launcher chaqirishni talab qiladi (~20-40 qator). Special ruxsatnomalar esa intent yaratish, sozlamalarga o'tish va `ON_RESUME` lifecycle kuzatuvchisini yozishni talab qiladi (~30-50 qator).

---

### 3-savol
**Javob:**
1. **Normal ruxsatnomalarga nega runtime dialog kerak emas:** Normal ruxsatnomalar foydalanuvchining shaxsiy ma'lumotlariga yoki qurilma xavfsizligiga bevosita xavf tug'dirmaydi, shuning uchun Android tizimi ularni o'rnatish vaqtida avtomatik ravishda beradi.
2. **Manifestdagi qator unutilsa beriladigan xato:** Runtime vaqtida tarmoq so'rovi amalga oshirilganda tizim `java.lang.SecurityException` tashlaydi va ilova halokatga uchraydi (crash).
3. **Normal ruxsatnomalarning 3 ta misoli:** `INTERNET`, `ACCESS_NETWORK_STATE`, `VIBRATE`.

---

### 4-savol
**Javob:**
1. **Signature ruxsatnomasi kimga beriladi:** Faqat ruxsatnomani e'lon qilgan ilova bilan **bir xil raqamli kalit (signing key / certificate)** yordamida imzolangan ilovalargagina beriladi.
2. **Manifestdagi protectionLevel:** `android:protectionLevel="signature"`.
3. **Natija va dialog:**
   - **Hamyon ilovasi:** Bank ilovasi bilan bir xil kalitda imzolangani uchun ruxsatnoma o'rnatish vaqtida avtomatik beriladi.
   - **Begona ilova:** Imzo mos kelmagani uchun ruxsat berilmaydi (`SecurityException`).
   - **Foydalanuvchiga dialog:** Har ikkala holatda ham foydalanuvchiga hech qanday dialog ko'rsatilmadi.

---

### 5-savol
**Javob:**
1. **Install-time ruxsatnomalar qachon va qayerda ko'rsatiladi:** Play Store'da ilovani yuklab olish sahifasida yoki ilova tafsilotlarida ko'rsatiladi. Tizim ularni ilova qurilmaga **o'rnatilayotgan soniyada (install-time)** avtomatik bir marta beradi.
2. **Ushbu toifaga kiruvchi ikki tur:**
   - **Normal permissions** (masalan, `INTERNET`).
   - **Signature permissions** (masalan, `signature` protection levelga ega maxsus ruxsatlar).

---

### 6-savol
**Javob:**
1. **Farq va sabab:** Runtime ruxsatnomalar **Android 6.0 (API 23, Marshmallow)** versiyasida joriy qilingan. API 22 (Android 5.1) da runtime dialog tizimi umuman mavjud emas, shuning uchun API 22 da dangerous turdagi ruxsatnomalar ham o'rnatish vaqtida (install-time) to'liq beriladi.
2. **`granted(kind, sdk)` modeli:**  
   $$\text{granted}(\text{kind}, \text{sdk}) = (\text{kind} == \text{InstallTime}) \lor (\text{kind} == \text{Runtime} \land \text{sdk} < 23) \lor (\text{kind} == \text{Runtime} \land \text{sdk} \ge 23 \land \text{userGranted})$$

---

### 7-savol
**Javob:**
1. **Runtime ruxsatnoma holatlari ketma-ketligi va `shouldShowRationale`:**
   - **1. Hali so'ralmagan:** `shouldShowRationale = false`, `isGranted = false`. Kod `launch()` bilan odatiy tizim dialogini chiqaradi.
   - **2. Bir marta rad etilgan:** `shouldShowRationale = true`, `isGranted = false`. Kod foydalanuvchiga nega ruxsat kerakligini tushuntiruvchi Rationale UI ko mezonini va "Qayta so'rash" tugmasini ko'rsatishi kerak.
   - **3. Ikki marta rad etilgan ("Don't ask again"):** `shouldShowRationale = false`, `isGranted = false`. Kod `launch()` ni chaqirsa dialog chiqmaydi; kod foydalanuvchiga "Sozlamalarni ochish" tugmasini ko'rsatishi kerak.
2. **Qayerga yo'naltirish kerak:** `Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)` orqali ilova sozlamalari sahifasiga.

---

### 8-savol
**Javob:**
1. **Uchta tarmoqning ishlashi:**
   - `status.isGranted`: Ruxsat berilgan bo'lsa ishlaydi — ekranda kamerasiz/kamerali asosiy funksional ko'rsatiladi.
   - `status.shouldShowRationale`: 1 marta rad etilganda ishlaydi — ekranda tushuntirish matni va "Ruxsat berish" tugmasi ko'rsatiladi.
   - `else`: Hali so'ralmagan yoki 2 marta rad etilgan holatda ishlaydi — 2 marta rad etilgan bo'lsa "Sozlamalarni ochish" tugmasi chiqadi.
2. **Survives recomposition:** `rememberPermissionState` Compose lifecycle'iga va Activity Result API'ga bog'langan holda holatni keshlaydi.
3. **`ExperimentalPermissionsApi`:** Accompanist permissions kutubxonasi tajribaviy maqomda ekanligini va kelajakda API o'zgarishi mumkinligini ogohlantiruvchi annotatsiya.

---

### 9-savol
**Javob:**
1. **Kutubxonasiz so'rov yozish:**
   ```kotlin
   val launcher = rememberLauncherForActivityResult(
       contract = ActivityResultContracts.RequestPermission()
   ) { isGranted -> ... }
   ```
2. **Joriy holatni o'qish:** `ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED`.
3. **Rationale'ni aniqlash:** `ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)`.
4. **Accompanist bilan solishtirish:** Accompanist holatlarni bitta ob'ektga o'rab beradi, ammo tashqi eksperimental kutubxonaga bog'lanadi. Standard API esa 100% barqaror, Jetpack standart API'si bo'lib, hech qanday qo'shimcha dependency talab qilmaydi.

---

### 10-savol
**Javob:**
1. **Contract qaytaradigan natija:** `Map<String, Boolean>` formatida qaytadi. Kalit — ruxsatnoma nomi (masalan `Manifest.permission.ACCESS_FINE_LOCATION`), qiymat — `true` (berilgan) yoki `false` (rad etilgan).
2. **Nega bitta interfeys:** Android 12+ tizimi aniq va taxminiy joylashuvni bitta dialog xaritasida Birlashgan Vizual Boshqaruv sifatida ko'rsatadi.
3. **Ikkala ruxsat rad etilsa:** Ilova joylashuvga bog'liq funksiyani o'chirishi yoki funksiya nega ishlamayotganini tushuntiruvchi xabar ko'rsatishi kerak.

---

### 11-savol
**Javob:**
Special ruxsatnomalar tizim dialogi orqali so'ralmaydi, ularni foydalanuvchi Sozlamalar menyusidan qo'lda yoqishi kerak.

| Ruxsatnoma | Nimaga ruxsat beradi | Tekshirish chaqiruvi |
| :--- | :--- | :--- |
| `SYSTEM_ALERT_WINDOW` | Boshqa ilovalar ustidan chizish (Overlay) | `Settings.canDrawOverlays(context)` |
| `WRITE_SETTINGS` | Tizim sozlamalarini o'zgartirish | `Settings.System.canWrite(context)` |
| `MANAGE_EXTERNAL_STORAGE` | Xotiradagi barcha fayllarga kirish | `Environment.isExternalStorageManager()` |
| `REQUEST_INSTALL_PACKAGES` | Noma'lum manbalardan paket o'rnatish | `context.packageManager.canRequestPackageInstalls()` |
| `ACCESS_BACKGROUND_LOCATION` | Fon rejimida joylashuvni olish | `ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)` (Faqat foreground berilgach) |

---

### 12-savol
**Javob:**
1. **"Tekshir → Sozlamalarni och → Qaytganda tekshir" andozasi:**
   Sozlamalar ekraniga o'tilganda ilova to'xtaydi (`onStop`). Sozlamalardan qaytganda hech qanday `onActivityResult` callback kelmaydi.
2. **Kod tuzatmasi (`LifecycleEventEffect`):**
   ```kotlin
   @Composable
   fun OverlayScreen() {
       val context = LocalContext.current
       var canDraw by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

       LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
           canDraw = Settings.canDrawOverlays(context)
       }

       Switch(
           checked = canDraw,
           onCheckedChange = { openOverlaySettings(context) }
       )
   }
   ```

---

### 13-savol
**Javob:**
1. **Ikki bosqichli tartib:**
   - **1-bosqich:** Avval `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` so'raladi.
   - **2-bosqich:** Faqat 1-bosqich ruxsati berilgandan keyingina `ACCESS_BACKGROUND_LOCATION` alohida so'raladi.
2. **Nega birga so'ralmaydi:** Android 11 (API 30)+ da ikkalasini birga so'rashni tizim taqiqlaydi va so'rovni darhol rad etadi.
3. **SDK_INT >= API 29 sharti:** Fon joylashuvi alohida ruxsatnoma sifatida API 29 (Android 10) da kiritilgan. API 28 va undan pastida asosiy joylashuv ruxsati fon rejimini ham o'z ichiga olgan.

---

### 14-savol
**Javob:**
1. **Permission group:** Mantiqan bir-biriga bog'liq bo'lgan ruxsatnomalar guruhi (masalan `LOCATION` guruhi ichida FINE va COARSE).
2. **Dialoglar sonini kamaytirish:** Guruhdagi bitta ruxsatnoma berilgan bo'lsa, xuddi shu guruhdagi ikkinchi ruxsatnoma so'ralganda tizim dialogni qayta ko'rsatmay avtomatik berishi yoki bitta muloqot oynasiga birlashtirishi mumkin.
3. **3 ta guruh misoli:** `CAMERA`, `LOCATION`, `CONTACTS`.
4. **Nega kodingizda guruhga tayanmaslik kerak:** Guruhlar tarkibi Android versiyalari va qurilma ishlab chiqaruvchilari (OEM) bo'yicha o'zgarishi mumkin. Kod har bir ruxsatnomani alohida so'rashi shart.

---

### 15-savol
**Javob:**
1. **Manifestda e'lon qilinmasa:** Ruxsat so'rovi darhol rad etiladi yoki ilova runtime'da `SecurityException` bilan crash bo'ladi.
2. **E'lon qilish farqi:** Install-time ruxsatnomalari faqat manifestda e'lon qilinadi. Runtime va Special ruxsatnomalari ham manifestda e'lon qilinishi, ham kodda tekshirilib so'ralishi shart.
3. **`android:maxSdkVersion` atributi:** Ruxsatnoma faqat ma'lum bir API versiyasigacha kerak bo'lganda ishlatiladi. Masalan: `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28"/>` (chunki API 29+ da Scoped Storage ishlatiladi).

---

### 16-savol
**Javob:**
1. **`required="true"` vs `required="false"`:**
   - `true`: Kamera uskunasi bo'lmagan qurilmalar (masalan Android TV) uchun Play Store ilovani yashiradi (o'rnatib bo'lmaydi).
   - `false`: Ilova barcha qurilmalarga o'rnatilishiga ruxsat beradi.
2. **Kodda tekshiruv:** `required="false"` bo'lganda kodda `context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)` orqali aparat mavjudligi majburiy tekshiriladi.
3. **`visibleOn` modelining 4 natijasi:**
   - `hasFeature=true, required=true` -> Do'konda ko'rinadi, o'rnatiladi.
   - `hasFeature=false, required=true` -> Do'konda yashiriladi, o'rnatilmaydi.
   - `hasFeature=true, required=false` -> Do'konda ko'rinadi, o'rnatiladi, funksiya ishlaydi.
   - `hasFeature=false, required=false` -> Do'konda ko'rinadi, o'rnatiladi, kamera funksiyasi UI'da o'chiriladi.

---

### 17-savol
**Javob:**
1. **Bir martalik ruxsatnoma (Android 11, API 30):** Foydalanuvchi "Only this time" ni tanlaganda ruxsat faqat ilova aktiv bo'lib turgan sessiya uchun beriladi. Ilova fonga o'tkazilganda yoki process yopilganda tizim ruxsatni avtomatik bekor qiladi.
2. **Nega granted keshlanmaydi:** `remember { ... }` qiymatni bir marta static saqlab qoladi. Fonga o'tib qaytganda ruxsat bekor bo'lgan bo'lsa ham `remember` da `true` qolib ketadi va app crash bo'ladi.
3. **ON_RESUME yechimi:**
   ```kotlin
   @Composable
   fun CameraScreen() {
       val context = LocalContext.current
       var hasCamera by remember { mutableStateOf(context.checkCameraPermission()) }

       LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
           hasCamera = context.checkCameraPermission()
       }
   }
   ```

---

### 18-savol
**Javob:**
1. **Auto-reset (Android 11+):** Foydalanuvchi ilovani bir necha oy ishlatmasa, tizim maxfiylikni himoya qilish uchun barcha berilgan runtime ruxsatlarni avtomatik bekor qiladi. Ilova qayta ochilganda odatiy ruxsat workflow'i ishlaydi. Foydalanuvchi buni `Intent(Settings.ACTION_AUTO_REVOKE_PERMISSIONS)` orqali o'chirib qo'yishi mumkin.
2. **`revokeSelfPermissionOnKill` (Android 13 / API 33):** Ilova endi kerak bo'lmagan runtime ruxsatnomalardan o'zi voz kechishi uchun ishlatiladi (masalan `context.revokeSelfPermissionOnKill(Manifest.permission.READ_CONTACTS)`). Bu foydalanuvchi xavfsizligi va minimal imtiyozlar (least privilege) tamoyiliga mos keladi.

---

### 19-savol
**Javob:**
1. **API versiyalari bo'yicha asosiy o'zgarishlar:**
   - **API 23 (Android 6.0):** Runtime Permissions (interaktiv muloqot oynasi) modeli kiritildi.
   - **API 30 (Android 11):** Bir martalik ("Only this time") ruxsatlar va ishlatilmagan ilovalar uchun Auto-reset mexanizmi joriy qilindi.
   - **API 33 (Android 13):** Media ruxsatnomalari bo'lindi (`READ_MEDIA_IMAGES`), `POST_NOTIFICATIONS` runtime ruxsatga aylantirildi va self-revocation API qo'shildi.
2. **POST_NOTIFICATIONS:** Android 13 da bildirishnoma yuborish runtime ruxsatnomaga aylandi. API 33 dan past versiyalarda u o'rnatish vaqtida (install-time) avtomatik beriladi, shuning uchun API 33 dan past versiyalarda dialog so'rash mantiqsiz va tizim launcher shartnomasi tomonidan qo'llab-quvvatlanmaydi.
