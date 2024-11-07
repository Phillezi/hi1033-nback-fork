package mobappdev.example.nback_cimpl.data.models

import java.util.Date

data class Score(
    val id: Int = 0,
    val playerName: String,
    val score: Int,
    val date: Date
)
