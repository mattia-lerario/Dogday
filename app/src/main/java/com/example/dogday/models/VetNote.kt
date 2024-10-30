package com.example.dogday.models

import java.util.UUID

data class VetNote(
    val id: String = UUID.randomUUID().toString(),
    val note: String = "",
)