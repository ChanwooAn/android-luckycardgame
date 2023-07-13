package hd.softeer.luckycardgame.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class WinnersInfo(
    val winnerNum: Int,
    val cardsNumbers: IntArray
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WinnersInfo

        if (winnerNum != other.winnerNum) return false
        if (!cardsNumbers.contentEquals(other.cardsNumbers)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = winnerNum
        result = 31 * result + cardsNumbers.contentHashCode()
        return result
    }
}