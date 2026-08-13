package com.zcm.hymnbook.data.model

/**
 * Fixed set of hymn categories used throughout the app.
 * Stored on HymnEntity as a plain String (category.label) so that
 * a future remote/admin system can add new categories without a
 * schema migration — the UI simply falls back to "Other" for any
 * unrecognized value.
 */
enum class HymnCategory(val label: String) {
    WORSHIP("Worship"),
    PRAISE("Praise"),
    PRAYER("Prayer"),
    COMMUNION("Communion"),
    EVANGELISM("Evangelism"),
    THANKSGIVING("Thanksgiving"),
    HOLY_SPIRIT("Holy Spirit"),
    CHRISTMAS("Christmas"),
    EASTER("Easter"),
    OTHER("Other");

    companion object {
        fun fromLabel(label: String): HymnCategory =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: OTHER

        fun allLabels(): List<String> = entries.map { it.label }
    }
}
