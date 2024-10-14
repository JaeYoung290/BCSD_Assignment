package com.example.test

fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    // 기본값 = 2
    val filterIndex = numbers.skipIndex()
    println(filterIndex)

    // n = 4
    val filterIndex4 = numbers.skipIndex(4)
    println(filterIndex4)
}

fun List<Int>.skipIndex(n: Int = 2): List<Int> {
    return this.filterIndexed { index, _ -> index % n == 0 }
}
