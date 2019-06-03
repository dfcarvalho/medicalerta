package br.com.dcarv.medicalerta.common

import androidx.emoji.text.EmojiCompat
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatTime(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat("HH:mm", locale)
    return formatter.format(this)
}

fun Date.format(locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
    return formatter.format(this)
}

fun Date.setDateOnly(date: Date) {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val newDateCalendar = Calendar.getInstance()
    newDateCalendar.time = date

    val year = newDateCalendar.get(Calendar.YEAR)
    val month = newDateCalendar.get(Calendar.MONTH)
    val day = newDateCalendar.get(Calendar.DAY_OF_MONTH)

    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, day)

    time = calendar.timeInMillis
}

fun Date.setTimeOnly(date: Date) {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val newDateCalendar = Calendar.getInstance()
    newDateCalendar.time = date

    val hour = newDateCalendar.get(Calendar.HOUR_OF_DAY)
    val minute = newDateCalendar.get(Calendar.MINUTE)

    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    time = calendar.timeInMillis
}

val Date.hour: Int
    get() {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = this
        return calendar.get(Calendar.HOUR)
    }

val Date.minute: Int
    get() {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = this
        return calendar.get(Calendar.MINUTE)
    }

val Date.emoji: String
    get() {
        val emoji = ClockEmoji.values().first {
            (it.hour == hour) && (it.thirty == (minute >= 30))
        }
        return String.format("%c", emoji.charCode)
    }

enum class ClockEmoji(val hour: Int, val thirty: Boolean, val charCode: Int) {
    OneOClock(1, false, 0x1F550),
    OneThirty(1, true, 0x1F55C),
    TwoOClock(2, false, 0x1F551),
    TwoThirty(2, true, 0x1F55D),
    ThreeOClock(3, false, 0x1F552),
    ThreeThirty(3, true, 0x1F55E),
    FourOClock(4, false, 0x1F553),
    FourThirty(4, true, 0x1F55F),
    FiveOClock(5, false, 0x1F554),
    FiveThirty(5, true, 0x1F560),
    SixOClock(6, false, 0x1F555),
    SixThirty(6, true, 0x1F561),
    SevenOClock(7, false, 0x1F556),
    SevenThirty(7, true, 0x1F562),
    EightOClock(8, false, 0x1F557),
    EightThirty(8, true, 0x1F563),
    NineOClock(9, false, 0x1F558),
    NineThirty(9, true, 0x1F564),
    TenOClock(10, false, 0x1F559),
    TenThirty(10, true, 0x1F565),
    ElevenOClock(11, false, 0x1F55A),
    ElevenThirty(11, true, 0x1F566),
    TwelveOClock(0, false, 0x1F55B),
    TwelveThirty(0, true, 0x1F567)
}