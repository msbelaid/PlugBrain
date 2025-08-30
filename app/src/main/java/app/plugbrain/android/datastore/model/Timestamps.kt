package app.plugbrain.android.datastore.model

data class Timestamps(
  val lastUsage: Long? = null,
  val lastBlock: Long? = null,
  val lastUnblock: Long? = null,
  val lastChallenge: Long? = null,
)
