import TileType.*
import java.awt.*
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneUI

val board = arrayListOf(
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE)),
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE)),
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE)),
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE)),
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE)),
    arrayListOf(Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL) ),
    arrayListOf(Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL) ),
    arrayListOf(Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(GOAL), Tile(WALL) ),
    arrayListOf(Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL) ),
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL) ),
    arrayListOf(Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(WALL), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE), Tile(EMPTY_SPACE))
)

var playerCoordinates = Pair(11, 8)
var boxCoordinates = arrayListOf(Pair(5, 7))
val goalCoordinates = arrayListOf(Pair(17, 7))

fun main() {
    board[playerCoordinates.second][playerCoordinates.first].hasPlayer = true
    boxCoordinates.forEach { board[it.second][it.first].hasBox = true }

    /*
    val frame = JFrame()
    val panel = JPanel(GridLayout(board[0].size, board.size))
    val field = JTextField(20)
    field.addKeyListener(MyKeyboard(panel))
    panel.add(field)
    frame.contentPane.add(panel)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(800, 600)
    frame.isVisible = true
    */

    printBoard()


    do {
        val move = inputMove()

        val newPlayerPosition = Pair(playerCoordinates.first + move.first, playerCoordinates.second + move.second)

        if (board[newPlayerPosition.second][newPlayerPosition.first].hasBox) {
            val newBoxPosition = Pair(newPlayerPosition.first + move.first, newPlayerPosition.second + move.second)
            if (board[newBoxPosition.second][newBoxPosition.first].type != WALL) {
                updateBoxPosition(newBoxPosition,newPlayerPosition)
                updatePlayerPosition(newPlayerPosition)
            }
        } else if (board[newPlayerPosition.second][newPlayerPosition.first].type != WALL) {
            updatePlayerPosition(newPlayerPosition)
        }

        printBoard()
    } while (!checkWin())


    println("Win!")
}

private fun checkWin(): Boolean {
    boxCoordinates.forEach { if(!goalCoordinates.contains(it)) return false}
    return true
}

private fun updatePlayerPosition(newPlayerPosition: Pair<Int, Int>) {
    board[playerCoordinates.second][playerCoordinates.first].hasPlayer = false
    playerCoordinates = newPlayerPosition
    board[playerCoordinates.second][playerCoordinates.first].hasPlayer = true
}

private fun updateBoxPosition(newBoxPosition: Pair<Int, Int>, oldBoxPosition: Pair<Int, Int>) {
    board[oldBoxPosition.second][oldBoxPosition.first].hasBox = false
    boxCoordinates.remove(oldBoxPosition)
    boxCoordinates.add(newBoxPosition)
    board[newBoxPosition.second][newBoxPosition.first].hasBox = true
}

private fun inputMove(): Pair<Int, Int> {
    return when(readln()) {
        "8" -> Pair(0, -1)
        "2" -> Pair(0, 1)
        "4" -> Pair(-1, 0)
        "6" -> Pair(1, 0)
        else -> {
            Pair(0, 0)
        }
    }
}

private fun printBoard() {
    for (row in board) {
        row.forEach { it.printChar() }
        println()
    }
}

class Data(player:Pair<Int,Int>, box: ArrayList<Pair<Int, Int>>, goal: ArrayList<Pair<Int, Int>>, board: ArrayList<ArrayList<Tile>>)

