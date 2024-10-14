package com.example.test

fun main() {
    val bcsdLabUser: List<BcsdLabUser> = listOf(
        BcsdMentor("양철수", "별", "asdf@gmail.com"),
        BcsdMentor("Mentor2", "별"),
        BcsdRegular("Regular1", "사과", true),
        BcsdRegular("양민준", "사과", false),
        BcsdRegular("양민지", "사과", false),
        BcsdBeginner("Beginner1", "새싹", 3),
        BcsdBeginner("Beginner2", "새싹", 1),
        BcsdBeginner("양현우", "새싹", 2),
        BcsdBeginner("Beginner4", "새싹", 5),
    )
    println("모든 비기너 목록:")
    bcsdLabUser.filterIsInstance<BcsdBeginner>().forEach { println(it.name) }
    println("과제를 3회 이상 수행하지 못한 비기너 목록:")
    bcsdLabUser.filterIsInstance<BcsdBeginner>().filter { it.missedAssignments >= 3 }
        .forEach { println(it.name) }
    println("이번달 회비를 내지 않은 레귤러 목록:")
    bcsdLabUser.filterIsInstance<BcsdRegular>().filter { !it.isPaidDues }
        .forEach { println(it.name) }
    println("성이 양씨인 사람 목록:")
    bcsdLabUser.filter { it.name.startsWith("양") }.forEach { println(it.name) }
}

open class BcsdLabUser(
    val name: String,
    val emoji: String
)

class BcsdMentor(
    name: String,
    emoji: String,
    var email: String? = null
) : BcsdLabUser(name, emoji)

class BcsdRegular(
    name: String,
    emoji: String,
    var isPaidDues: Boolean
) : BcsdLabUser(name, emoji)

class BcsdBeginner(
    name: String,
    emoji: String,
    var missedAssignments: Int
) : BcsdLabUser(name, emoji)
