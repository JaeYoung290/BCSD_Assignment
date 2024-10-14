package com.example.test

fun main() {
    val list1 = listOf(1, 2, 3)
    val list2 = listOf(2, 3, 4)

    val list3 = list1.toMutableList()
    val difference = list2.subtract(list1)
    list3.addAll(difference)
    list3.sort()


    println(list3) //It might be [1, 2, 3, 4]
}