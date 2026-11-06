package app.plugbrain.android.screenshots

/** The small uppercase line and the headline above each framed Google Play screenshot. */
data class PlayCaption(
    val eyebrow: String,
    val headline: String,
)

/**
 * Captions for the seven Google Play screenshots, per fastlane locale folder, in screenshot order.
 * The seventh headline is the app's own welcome title, so it isn't listed here.
 * Translated for meaning, in the same tone as the app (informal, formal आप in Hindi).
 */
val playCaptions: Map<String, List<PlayCaption>> =
    mapOf(
        "en-US" to
            listOf(
                PlayCaption("Distracting app? Not so fast.", "Solve a little math. Scroll a lot less."),
                PlayCaption("Adaptive difficulty", "The more you scroll, the harder it gets."),
                PlayCaption("Take a real break", "Step away, and it gets easier again."),
                PlayCaption("You pick", "Lock the apps that steal your time."),
                PlayCaption("Your rules", "Choose how often, and how hard."),
                PlayCaption("Free & open source", "No ads. No tracking. No internet needed."),
                PlayCaption("Light or dark", ""),
            ),
        "ar" to
            listOf(
                PlayCaption("تطبيق مشتّت؟ مهلًا", "مسألة صغيرة، وتمرير أقل بكثير."),
                PlayCaption("صعوبة تتكيّف معك", "كلما تصفحت أكثر، صار أصعب."),
                PlayCaption("خذ استراحة حقيقية", "ابتعد قليلًا، ويعود أسهل من جديد."),
                PlayCaption("أنت تختار", "اقفل التطبيقات التي تسرق وقتك."),
                PlayCaption("قواعدك أنت", "اختر كم مرة، وبأي صعوبة."),
                PlayCaption("مجاني ومفتوح المصدر", "بلا إعلانات. بلا تتبّع. بلا إنترنت."),
                PlayCaption("فاتح أو داكن", ""),
            ),
        "de-DE" to
            listOf(
                PlayCaption(
                    "Ablenkende App? Nicht so schnell.",
                    "Ein bisschen Mathe. Viel weniger Scrollen.",
                ),
                PlayCaption("Mitwachsende Schwierigkeit", "Je mehr du scrollst, desto schwerer wird’s."),
                PlayCaption("Mach echte Pausen", "Bleib weg, und es wird wieder leichter."),
                PlayCaption("Du entscheidest", "Sperr die Apps, die dir Zeit rauben."),
                PlayCaption("Deine Regeln", "Wähl, wie oft und wie schwer."),
                PlayCaption("Kostenlos & Open Source", "Keine Werbung. Kein Tracking. Kein Internet."),
                PlayCaption("Hell oder dunkel", ""),
            ),
        "fa" to
            listOf(
                PlayCaption("برنامه‌ی حواس‌پرت‌کن؟ نه به این زودی", "کمی ریاضی، خیلی کمتر اسکرول."),
                PlayCaption("سختی هوشمند", "هرچه بیشتر اسکرول کنی، سخت‌تر می‌شود."),
                PlayCaption("یک استراحت واقعی", "کمی فاصله بگیر تا دوباره آسان شود."),
                PlayCaption("انتخاب با توست", "برنامه‌هایی را که وقتت را می‌دزدند قفل کن."),
                PlayCaption("قانون‌های خودت", "انتخاب کن چند وقت یک بار و چقدر سخت."),
                PlayCaption("رایگان و متن‌باز", "بدون تبلیغ. بدون ردیابی. بدون اینترنت."),
                PlayCaption("روشن یا تیره", ""),
            ),
        "fr-FR" to
            listOf(
                PlayCaption(
                    "Appli qui distrait ? Pas si vite.",
                    "Un petit calcul. Beaucoup moins de scroll.",
                ),
                PlayCaption("Difficulté adaptative", "Plus tu scrolles, plus c’est dur."),
                PlayCaption("Fais une vraie pause", "Éloigne-toi, et ça redevient facile."),
                PlayCaption("C’est toi qui choisis", "Bloque les applis qui te volent ton temps."),
                PlayCaption("Tes règles", "Choisis à quelle fréquence, et à quel niveau."),
                PlayCaption("Libre et open source", "Pas de pub. Pas de pistage. Pas d’internet."),
                PlayCaption("Clair ou sombre", ""),
            ),
        "hi" to
            listOf(
                PlayCaption("ध्यान भटकाने वाला ऐप? ज़रा रुकिए", "थोड़ा-सा गणित, बहुत कम स्क्रॉल।"),
                PlayCaption("बदलती कठिनाई", "जितना ज़्यादा स्क्रॉल, उतना मुश्किल।"),
                PlayCaption("सच्चा ब्रेक लें", "थोड़ा दूर रहें, फिर से आसान हो जाएगा।"),
                PlayCaption("आपकी पसंद", "समय चुराने वाले ऐप्स लॉक करें।"),
                PlayCaption("आपके नियम", "कितनी बार और कितना कठिन, आप चुनें।"),
                PlayCaption("मुफ़्त और ओपन सोर्स", "न विज्ञापन, न ट्रैकिंग, न इंटरनेट।"),
                PlayCaption("लाइट या डार्क", ""),
            ),
        "ja" to
            listOf(
                PlayCaption("気が散るアプリ？ちょっと待って", "ちょっと計算、スクロールはぐっと減る。"),
                PlayCaption("変化する難易度", "スクロールするほど、難しくなる。"),
                PlayCaption("ちゃんと休憩", "離れれば、また易しくなる。"),
                PlayCaption("あなたが選ぶ", "時間を奪うアプリをロック。"),
                PlayCaption("あなたのルール", "頻度も難しさも、自分で決める。"),
                PlayCaption("無料＆オープンソース", "広告なし。追跡なし。ネット不要。"),
                PlayCaption("ライトもダークも", ""),
            ),
        "nb-NO" to
            listOf(
                PlayCaption("Distraherende app? Ikke så fort.", "Litt matte. Mye mindre scrolling."),
                PlayCaption("Tilpasset vanskelighetsgrad", "Jo mer du scroller, jo vanskeligere blir det."),
                PlayCaption("Ta en ekte pause", "Hold deg unna, så blir det lettere igjen."),
                PlayCaption("Du velger", "Lås appene som stjeler tiden din."),
                PlayCaption("Dine regler", "Velg hvor ofte, og hvor vanskelig."),
                PlayCaption("Gratis og åpen kildekode", "Ingen reklame. Ingen sporing. Ikke noe nett."),
                PlayCaption("Lyst eller mørkt", ""),
            ),
        "nl-NL" to
            listOf(
                PlayCaption("Afleidende app? Niet zo snel.", "Een beetje rekenen. Veel minder scrollen."),
                PlayCaption("Meegroeiende moeilijkheid", "Hoe meer je scrollt, hoe moeilijker het wordt."),
                PlayCaption("Neem echt pauze", "Blijf weg, en het wordt weer makkelijker."),
                PlayCaption("Jij kiest", "Vergrendel de apps die je tijd opslokken."),
                PlayCaption("Jouw regels", "Kies hoe vaak, en hoe moeilijk."),
                PlayCaption("Gratis en open source", "Geen advertenties. Geen tracking. Geen internet."),
                PlayCaption("Licht of donker", ""),
            ),
        "ru-RU" to
            listOf(
                PlayCaption(
                    "Отвлекающее приложение? Не так быстро",
                    "Немного математики. Гораздо меньше скроллинга.",
                ),
                PlayCaption("Сложность растёт", "Чем больше листаешь, тем сложнее."),
                PlayCaption("Настоящий перерыв", "Отвлекись, и станет снова легче."),
                PlayCaption("Выбираешь ты", "Заблокируй приложения, которые крадут время."),
                PlayCaption("Твои правила", "Выбери, как часто и насколько сложно."),
                PlayCaption("Бесплатно и открыто", "Без рекламы. Без слежки. Без интернета."),
                PlayCaption("Светлая или тёмная", ""),
            ),
        "zh-CN" to
            listOf(
                PlayCaption("想刷分心应用？先等等", "做点小算术，少刷一大半。"),
                PlayCaption("难度自适应", "刷得越多，题越难。"),
                PlayCaption("好好休息一下", "离开一会儿，难度就会降下来。"),
                PlayCaption("由你决定", "锁住偷走你时间的应用。"),
                PlayCaption("你的规则", "多久挑战一次、有多难，你说了算。"),
                PlayCaption("免费开源", "无广告，无追踪，无需联网。"),
                PlayCaption("浅色或深色", ""),
            ),
    )
