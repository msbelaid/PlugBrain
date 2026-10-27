package app.plugbrain.android.ui.about

/** Pages outside the app that the About sheet links to. */
enum class AboutLink(val url: String) {
  SourceCode("https://github.com/msbelaid/PlugBrain"),
  ReportBug("https://github.com/msbelaid/PlugBrain/issues"),
  Website("https://plugbrain.app"),
}
