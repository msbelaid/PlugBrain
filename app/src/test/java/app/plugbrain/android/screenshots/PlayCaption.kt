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
 * A \n sets where a headline breaks; without one it wraps to fit.
 */
val playCaptions: Map<String, List<PlayCaption>> =
    mapOf(
        "en-US" to
            listOf(
                PlayCaption("Distracting app? Not so fast.", "Solve a little math.\nScroll a lot less."),
                PlayCaption("Adaptive difficulty", "The more you scroll,\nthe harder it gets."),
                PlayCaption("Take a real break", "Step away, and it\ngets easier again."),
                PlayCaption("You pick", "Lock the apps that\nsteal your time."),
                PlayCaption("Your rules", "Choose how often,\nand how hard."),
                PlayCaption("Free & open source", "No ads. No tracking.\nNo internet needed."),
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
                    "Ein bisschen Mathe.\nViel weniger Scrollen.",
                ),
                PlayCaption("Mitwachsende Schwierigkeit", "Je mehr du scrollst,\ndesto schwerer wird’s."),
                PlayCaption("Mach echte Pausen", "Bleib weg, und es wird\nwieder leichter."),
                PlayCaption("Du entscheidest", "Sperr die Apps,\ndie dir Zeit rauben."),
                PlayCaption("Deine Regeln", "Wähl, wie oft\nund wie schwer."),
                PlayCaption("Kostenlos & Open Source", "Keine Werbung. Kein Tracking. Kein Internet."),
                PlayCaption("Hell oder dunkel", ""),
            ),
        "es-ES" to
            listOf(
                PlayCaption(
                    "¿Una app que distrae? No tan rápido.",
                    "Un poco de matemáticas.\nMucho menos scroll.",
                ),
                PlayCaption("Dificultad adaptativa", "Cuanto más scroll,\nmás difícil se pone."),
                PlayCaption("Un descanso de verdad", "Aléjate un rato, y\nvuelve a ser fácil."),
                PlayCaption("Tú eliges", "Bloquea las apps que\nte roban el tiempo."),
                PlayCaption("Tus reglas", "Elige la frecuencia\ny la dificultad."),
                PlayCaption("Gratis y de código abierto", "Sin anuncios. Sin rastreo.\nSin internet."),
                PlayCaption("Claro u oscuro", ""),
            ),
        "fa" to
            listOf(
                PlayCaption("برنامه‌ی حواس‌پرت‌کن؟ نه به این زودی", "کمی ریاضی، خیلی کمتر اسکرول."),
                PlayCaption("سختی هوشمند", "هرچه بیشتر اسکرول کنی،\nسخت‌تر می‌شود."),
                PlayCaption("یک استراحت واقعی", "کمی فاصله بگیر\nتا دوباره آسان شود."),
                PlayCaption("انتخاب با توست", "برنامه‌هایی را که وقتت را\nمی‌دزدند قفل کن."),
                PlayCaption("قانون‌های خودت", "انتخاب کن چند وقت یک بار\nو چقدر سخت."),
                PlayCaption("رایگان و متن‌باز", "بدون تبلیغ. بدون ردیابی.\nبدون اینترنت."),
                PlayCaption("روشن یا تیره", ""),
            ),
        "fr-FR" to
            listOf(
                PlayCaption(
                    "Appli qui distrait ? Pas si vite.",
                    "Un petit calcul.\nBien moins de scroll.",
                ),
                PlayCaption("Difficulté adaptative", "Plus tu scrolles,\nplus c’est dur."),
                PlayCaption("Fais une vraie pause", "Éloigne-toi, et ça\nredevient facile."),
                PlayCaption("C’est toi qui choisis", "Bloque les applis qui\nte volent ton temps."),
                PlayCaption("Tes règles", "Choisis le rythme\net la difficulté."),
                PlayCaption("Libre et open source", "Ni pub, ni pistage,\nni internet."),
                PlayCaption("Clair ou sombre", ""),
            ),
        "hi" to
            listOf(
                PlayCaption("ध्यान भटकाने वाला ऐप? ज़रा रुकिए", "थोड़ा-सा गणित, बहुत कम स्क्रॉल।"),
                PlayCaption("बदलती कठिनाई", "जितना ज़्यादा स्क्रॉल,\nउतना मुश्किल।"),
                PlayCaption("सच्चा ब्रेक लें", "थोड़ा दूर रहें,\nफिर से आसान हो जाएगा।"),
                PlayCaption("आपकी पसंद", "समय चुराने वाले ऐप्स लॉक करें।"),
                PlayCaption("आपके नियम", "कितनी बार और कितना कठिन,\nआप चुनें।"),
                PlayCaption("मुफ़्त और ओपन सोर्स", "न विज्ञापन, न ट्रैकिंग, न इंटरनेट।"),
                PlayCaption("लाइट या डार्क", ""),
            ),
        "ja" to
            listOf(
                PlayCaption("気が散るアプリ？ちょっと待って", "ちょっと計算、\nスクロールはぐっと減る。"),
                PlayCaption("変化する難易度", "スクロールするほど、\n難しくなる。"),
                PlayCaption("ちゃんと休憩", "離れれば、また易しくなる。"),
                PlayCaption("あなたが選ぶ", "時間を奪うアプリをロック。"),
                PlayCaption("あなたのルール", "頻度も難しさも、\n自分で決める。"),
                PlayCaption("無料＆オープンソース", "広告なし。追跡なし。\nネット不要。"),
                PlayCaption("ライトもダークも", ""),
            ),
        "nb-NO" to
            listOf(
                PlayCaption("Distraherende app? Ikke så fort.", "Litt matte.\nMye mindre scrolling."),
                PlayCaption("Tilpasset vanskelighetsgrad", "Jo mer du scroller, jo vanskeligere blir det."),
                PlayCaption("Ta en ekte pause", "Hold deg unna, så\nblir det lettere igjen."),
                PlayCaption("Du velger", "Lås appene som\nstjeler tiden din."),
                PlayCaption("Dine regler", "Velg hvor ofte,\nog hvor vanskelig."),
                PlayCaption("Gratis og åpen kildekode", "Ingen reklame. Ingen sporing. Ikke noe nett."),
                PlayCaption("Lyst eller mørkt", ""),
            ),
        "nl-NL" to
            listOf(
                PlayCaption("Afleidende app? Niet zo snel.", "Een beetje rekenen.\nVeel minder scrollen."),
                PlayCaption("Meegroeiende moeilijkheid", "Hoe meer je scrollt,\nhoe moeilijker het wordt."),
                PlayCaption("Neem echt pauze", "Blijf weg, en het\nwordt weer makkelijker."),
                PlayCaption("Jij kiest", "Vergrendel de apps\ndie je tijd opslokken."),
                PlayCaption("Jouw regels", "Kies hoe vaak,\nen hoe moeilijk."),
                PlayCaption("Gratis en open source", "Geen advertenties. Geen tracking. Geen internet."),
                PlayCaption("Licht of donker", ""),
            ),
        "pt-BR" to
            listOf(
                PlayCaption(
                    "App que distrai? Calma lá.",
                    "Uma continha rápida.\nBem menos rolagem.",
                ),
                PlayCaption("Dificuldade adaptativa", "Quanto mais você rola,\nmais difícil fica."),
                PlayCaption("Uma pausa de verdade", "Dê um tempo, e tudo\nfica fácil de novo."),
                PlayCaption("Você escolhe", "Bloqueie os apps que\nroubam seu tempo."),
                PlayCaption("Suas regras", "Escolha a frequência\ne a dificuldade."),
                PlayCaption("Grátis e de código aberto", "Sem anúncios,\nsem rastreio, offline."),
                PlayCaption("Claro ou escuro", ""),
            ),
        "ru-RU" to
            listOf(
                PlayCaption(
                    "Отвлекающее приложение? Не так быстро",
                    "Немного математики.\nМеньше скроллинга.",
                ),
                PlayCaption("Сложность растёт", "Чем больше листаешь,\nтем сложнее."),
                PlayCaption("Настоящий перерыв", "Отвлекись,\nи станет снова легче."),
                PlayCaption("Выбираешь ты", "Заблокируй приложения,\nкоторые крадут время."),
                PlayCaption("Твои правила", "Выбери, как часто\nи насколько сложно."),
                PlayCaption("Бесплатно и открыто", "Без рекламы. Без слежки.\nБез интернета."),
                PlayCaption("Светлая или тёмная", ""),
            ),
        "zh-CN" to
            listOf(
                PlayCaption("想刷分心应用？先等等", "做点小算术，少刷一大半。"),
                PlayCaption("难度自适应", "刷得越多，题越难。"),
                PlayCaption("好好休息一下", "离开一会儿，\n难度就会降下来。"),
                PlayCaption("由你决定", "锁住偷走你时间的应用。"),
                PlayCaption("你的规则", "多久挑战一次、有多难，\n你说了算。"),
                PlayCaption("免费开源", "无广告，无追踪，无需联网。"),
                PlayCaption("浅色或深色", ""),
            ),
    )
