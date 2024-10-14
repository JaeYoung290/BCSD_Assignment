package com.example.test

fun main() {
    val testMentor = Mentor("김지민", "별", "asdf@gmail.com")
    val testMentorEmptyEmail = Mentor("김현수", "별")
    val testRegular = Regular("김준호", "사과", true)
    val testBeginner = Beginner("김철수", "새싹", 1)

    println("Mentor: ${testMentor.name}\n이모지: ${testMentor.emoji}\n이메일: ${testMentor.email}\n")
    println("Mentor: ${testMentorEmptyEmail.name}\n이모지: ${testMentorEmptyEmail.emoji}\n이메일: ${testMentorEmptyEmail.email}\n")
    println("Regular: ${testRegular.name}\n이모지: ${testRegular.emoji}\n회비 납부 여부: ${testRegular.isPaidDues}\n")
    println("Beginner: ${testBeginner.name}\n이모지: ${testBeginner.emoji}\n과제 미수행 횟수: ${testBeginner.missedAssignments}\n")
}

abstract class Member(
    val name: String,
    val emoji: String
)

class Mentor(
    name: String,
    emoji: String,
    var email: String? = null
) : Member(name, emoji)

class Regular(
    name: String,
    emoji: String,
    var isPaidDues: Boolean
) : Member(name, emoji)

class Beginner(
    name: String,
    emoji: String,
    var missedAssignments: Int
) : Member(name, emoji)

