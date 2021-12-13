package io.github.kabirnayeem99.dumarketingadmin.common.ktx

fun <T> ArrayList<T>.toCommaSeparatedList(): String {
    var commaSeparatedString = ""

    for ((index, i) in this.withIndex()) {
        commaSeparatedString = if (index != 0) "$commaSeparatedString, $i" else i.toString()
    }
    return commaSeparatedString
}