package ru.university.taskcalendar

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// В базе дата хранится как "2026-10-06" (удобно сортировать и сравнивать),
// а пользователю показываем привычное "06.10.2026".
object DateUtils {

    private val uiFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun toUi(dbDate: String): String = LocalDate.parse(dbDate).format(uiFormat)

    // Возвращает null, если пользователь ввёл дату неправильно
    fun toDb(uiDate: String): String? = try {
        LocalDate.parse(uiDate.trim(), uiFormat).toString()
    } catch (e: DateTimeParseException) {
        null
    }

    fun todayUi(): String = LocalDate.now().format(uiFormat)
}