import Moves.*
import TileType.*
import sa.*

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

    val data = Data(playerCoordinates, boxCoordinates, goalCoordinates, INVALID.cord, board)

    while (true) {
        println("choose game mode(by number):")
        println("+---+------------+")
        println("| 1 |   player   |")
        println("+---+------------+")
        println("| 2 |     ia     |")
        println("+---+------------+")
        when (readln()) {
            "1" -> player(data)
            "2" -> ia(data)
            else -> continue
        }
        break
    }
}

fun ia(data: Data) {
    val solution = sa(1.0,1e-7,1e-3,5, data, ::getInitialSolution, ::getRandomNeigh, ::evalFunc, ::isOptimum, Sense.MINIMIZE)
    /*
    optimiseSolution(solution.u).forEach {
        printBoard(it.board)
        Thread.sleep(500)
    }
     */
    printBoard(solution.u.last().board)
    println("Total moves: ${solution.s.cost}")
}

private fun player(data: Data) {
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
    printBoard(board)

    do {
        makeMove(inputMove(), data)
        printBoard(data.board)
    } while (!checkWin(data))

    println("Win!")
}

fun checkWin(data: Data): Boolean {
    data.box.forEach { if(!data.goal.contains(it)) return false}
    return true
}

private fun inputMove(): Pair<Int, Int> {
    return when(readln()) {
        "8" -> UP
        "2" -> DOWN
        "4" -> LEFT
        "6" -> RIGHT
        else -> INVALID
    }.cord
}

fun makeMove(move: Pair<Int, Int>, data: Data) {
    val newPlayerPosition = Pair(data.player.first + move.first, data.player.second + move.second)

    if (data.board[newPlayerPosition.second][newPlayerPosition.first].hasBox) {
        val newBoxPosition = Pair(newPlayerPosition.first + move.first, newPlayerPosition.second + move.second)
        if (data.board[newBoxPosition.second][newBoxPosition.first].type != WALL) {
            data.updateBoxPosition(newBoxPosition, newPlayerPosition)
            data.updatePlayerPosition(newPlayerPosition)
        }
    } else if (data.board[newPlayerPosition.second][newPlayerPosition.first].type != WALL) {
        data.updatePlayerPosition(newPlayerPosition)
    }
    data.move = move
}

private fun printBoard(board: ArrayList<ArrayList<Tile>>) {
    for (row in board) {
        row.forEach { it.printChar() }
        println()
    }
}

fun optimiseSolution(input: ArrayList<Data>): ArrayList<Data> {
    val solution = input.clone() as ArrayList<Data>
    var i = 0
    while (i != solution.size - 2) {
        if (solution[i].move == -solution[i + 1].move && solution[i].box == solution[i + 1].box) {
            solution.removeAt(i+1)
            solution.removeAt(i)
            --i
        } else if (solution[i].move == -solution[i + 2].move && solution[i].move != solution[i + 1].move
            && solution[i].box == solution[i + 2].box) {
            solution.removeAt(i+2)
            solution.removeAt(i)
            --i
        } else
            ++i
    }
    return solution
}

