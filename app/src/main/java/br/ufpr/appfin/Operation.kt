package br.ufpr.appfin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Operation(
    val typoOpe:String,
    val desc:String,
    val valor:Double,
    val data: LocalDateTime
) : Parcelable
