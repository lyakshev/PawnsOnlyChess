package chess

import kotlin.math.abs

var firstName = ""
var secondName = ""
var input = ""
var turn = "W"
val xList = "abcdefgh"
var x1 = 0
var y1 = 0
var x2 = 0
var y2 = 0
var previousMove = mutableListOf<Int>(0,0,0,0)

val cB = mutableListOf(
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[a]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[b]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[c]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[d]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[e]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[f]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[g]
    mutableListOf<String>("", "W", "", "", "", "", "B", ""),   //[h]
)

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    firstName = readln()
    println("Second Player's name:")
    secondName = readln()
    printChess()
    play()
}

fun checkWin() {
    var winB = 0
    var winW = 0

    for (i in 0..7 ) {
        if (cB[i][0] !="") {
            println("Black Wins!")
            print("Bye!")
            input = "exit"
        }
        if (cB[i][7] !="") {
            println("White Wins!")
            print("Bye!")
            input = "exit"
        }
    }

    for (i in 0..7) {
        for (j in 0..7) {
            when (cB[i][j]) {
                "W" -> winW++
                "B" -> winB++
            }
        }
    }

    if (winB == 0) {
        println("White Wins!")
        print("Bye!")
        input = "exit"
    }

    if (winW == 0) {
        println("Black Wins!")
        print("Bye!")
        input = "exit"
    }
}

fun checkStalemate() {

    var availableMove = 0

    for (i in 0..7) {
        for (j in 0..7) {
            if (turn == "W" && cB[i][j] == "W" && cB[i][j+1] == "") availableMove++
            if (turn == "B" && cB[i][j] == "B" && cB[i][j-1] == "") availableMove++

            if (turn == "W" && cB[i][j] == "W" && i == 0 && cB[i+1][j+1] == "B") availableMove++
            if (turn == "W" && cB[i][j] == "W" && i != 0 && i !=7 && (cB[i+1][j+1] == "B" || cB[i-1][j+1] == "B")) availableMove++
            if (turn == "W" && cB[i][j] == "W" && i == 7 && cB[i-1][j+1] == "B") availableMove++

            if (turn == "B" && cB[i][j] == "B" && i == 0 && cB[i+1][j-1] == "W") availableMove++
            if (turn == "B" && cB[i][j] == "B" && i != 0 && i !=7 && (cB[i+1][j-1] == "W" || cB[i-1][j-1] == "W")) availableMove++
            if (turn == "B" && cB[i][j] == "B" && i == 7 && cB[i-1][j-1] == "W") availableMove++

        }
    }

    if (availableMove == 0) {
        println("Stalemate!")
        print("Bye!")
        input = "exit"
    }
}


fun nextTurnOrEnd() {
    checkWin()

    turn = if (turn == "W") "B" else "W"
    previousMove[0] = x1
    previousMove[1] = y1
    previousMove[2] = x2
    previousMove[3] = y2

    checkStalemate()
}

fun pawnMove() {
    cB[x1][y1] = ""
    cB[x2][y2] = turn
}

fun play() {
    do {
        println("${if (turn == "W") firstName else secondName}'s turn:")
        input = readln()

        when {
            input == "exit" -> print("Bye!")
            coordinatesOnBoard() -> {
                coordinatesToXY()
                when (moveOrCapture()) {
                    1 -> {//Move
                        if (correctFirstCoordinate() && correctSecondCoordinateToMove()) {
                            pawnMove()
                            printChess()
                            nextTurnOrEnd()
                        }
                    }
                    2 -> {//Capture
                        if (correctFirstCoordinate() && correctSecondCoordinateToCapture()) {
                            pawnMove()
                            printChess()
                            nextTurnOrEnd()
                        }
                    }
                    else -> {
                        println("Invalid Input")
                    }
                }
            }
            else -> println("Invalid Input")
        }
    } while (input != "exit")
}

fun moveOrCapture() : Int{
    return when {
        x2 == x1 &&  abs(y2 - y1) <= 2 -> 1 //Move
        abs(x2 - x1) == 1 && abs(y2 - y1) == 1 -> 2 //Capture
        else -> 3 //Invalid input
    }
}

fun coordinatesOnBoard() : Boolean {
    return input.matches(Regex("[a-h][1-8][a-h][1-8]"))
}

fun coordinatesToXY() {
    for (i in 0..7) {if (input[0] == xList[i]) x1 = i}
    y1 = input[1].toString().toInt() - 1
    for (i in 0..7) {if (input[2] == xList[i]) x2 = i}
    y2 = input[3].toString().toInt() - 1

}

fun correctFirstCoordinate(): Boolean {
    return when (cB[x1][y1]) {
        "B" -> {
            if (turn == "W") {
                println("No white pawn at ${xList[x1]}${y1+1}")
                false
            } else true
        }
        "W" -> {
            if (turn == "W") true else {
                println("No black pawn at ${xList[x1]}${y1+1}")
                false
            }
        }
        else -> {
                println("No ${if (turn == "W") "white" else "black"} pawn at ${xList[x1]}${y1+1}")
                false
        }
    }
}

fun correctSecondCoordinateToMove() : Boolean{
    return if (x2 == x1) {
        when {
            turn == "W" && (y2 - y1 == 1) && (cB[x2][y2] == "") -> true
            turn == "W" && y1 == 1 && y2 == 3 && (cB[x2][3] == "") && (cB[x2][2] == "") -> true
            turn =="B" && (y2 - y1 == -1) && (cB[x2][y2] == "") -> true
            turn == "B" && y1 == 6 && y2 == 4 && (cB[x2][5] == "") && (cB[x2][4] == "") -> true
            else -> {
                println("Invalid Input")
                false
            }
        }
    } else {
        println("Invalid Input")
        false
    }
}

fun correctSecondCoordinateToCapture() : Boolean {
    return when {
        turn == "W" && (y2 - y1 == 1) && abs(x2 - x1) == 1 && cB[x2][y2] == "B" -> true
        turn =="B" && (y2 - y1 == -1) && abs(x2 - x1) == 1 && cB[x2][y2] == "W"-> true
        turn == "W" && y1 == 4 && y2 == 5 && abs(x2 - x1) == 1 && cB[x2][y2] == "" && previousMove[0] == x2 && previousMove[0] == previousMove[2] && previousMove[1] == 6 && previousMove [3] == 4 -> {
            cB[previousMove[2]][previousMove[3]] = ""
            true
        }
        turn =="B" && y1 ==3 && y2 ==2 && abs(x2 - x1) == 1 && cB[x2][y2] == "" && previousMove[0] == x2 && previousMove[0] == previousMove[2] && previousMove[1] == 1 && previousMove [3] == 3-> {
            cB[previousMove[2]][previousMove[3]] = ""
            true
        }
        else -> {
            println("Invalid Input")
            false
        }
    }
}

fun printChess() {

    val razd = "  +---+---+---+---+---+---+---+---+"

    println(razd)
    for (j in 7 downTo 0){
        print("${j+1} |")
        for (i in 0..7) print(" ${if (cB[i][j] == "") " " else cB[i][j]} |")
        println("\n$razd")
    }
    println("    a   b   c   d   e   f   g   h")

}