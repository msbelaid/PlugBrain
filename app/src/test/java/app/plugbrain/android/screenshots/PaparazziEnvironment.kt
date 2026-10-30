package app.plugbrain.android.screenshots

import app.cash.paparazzi.Environment
import app.cash.paparazzi.detectEnvironment

// Paparazzi 1.3.5 renders at the app's targetSdk (36), but its layoutlib lacks the API 35
// text and view methods Compose 1.8+ calls there. Rendering at API 34 keeps Compose off them.
private const val RENDER_API_LEVEL = 34

fun paparazziEnvironment(): Environment =
    detectEnvironment().copy(compileSdkVersion = RENDER_API_LEVEL)
