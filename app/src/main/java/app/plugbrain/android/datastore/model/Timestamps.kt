package app.plugbrain.android.datastore.model

data class Timestamps(
  val lastUsage: Long?,
  val lastBlock: Long?,
  val lastUnblock: Long?,
  val lastChallenge: Long?,
)
