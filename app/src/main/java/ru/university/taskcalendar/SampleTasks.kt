package ru.university.taskcalendar


object SampleTasks {

    val tasks = listOf(
        Task(1, "Подготовить отчёт по практике", "Описать все этапы и приложить скриншоты", "06.10.2026"),
        Task(2, "Разобраться с RecyclerView", "Adapter, ViewHolder, LayoutManager", "07.10.2026", isDone = true),
        Task(3, "Купить продукты", "Молоко, хлеб, яйца, овощи", "08.10.2026"),
        Task(4, "Записаться в спортзал", "Узнать расписание и цены", "10.10.2026"),
        Task(5, "Позвонить научному руководителю", "Согласовать тему курсовой", "12.10.2026")
    )

    fun findById(id: Int): Task? = tasks.find { it.id == id }
}