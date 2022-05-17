import Moves.*
import TileType.*
import kotlin.math.roundToInt

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
    while (true) {
        println("choose game mode(by number):")
        println("+---+------------+")
        println("| 1 |   player   |")
        println("+---+------------+")
        println("| 2 |     ia     |")
        println("+---+------------+")
        when (readln()) {
            "1" -> player()
            "2" -> ia()
            else -> continue
        }
        break
    }
}

fun ia() {
    val data = Data(playerCoordinates, boxCoordinates, goalCoordinates, board)

    sa(35.0,0.0,0.001,5, data, ::getInitialSolution, ::getRandomNeigh, ::evalFunc, ::isOptimum, Sense.MINIMIZE)
    TODO("Not yet implemented")
}

fun evalFunc(u: U, data: Data): Double {
    val tmp = u.solution.last()
    val goals = tmp.goal.clone() as ArrayList<Pair<Int, Int>>
    var total = 0.0
    tmp.box.forEach {
        var closestGoal = goals.first()
        var closestDistance = Int.MAX_VALUE
        for (goal in goals) {
            val distance = Math.abs(it.first - goal.first) + Math.abs(it.second - goal.second)
            if (distance < closestDistance) {
                closestDistance = distance
                closestGoal = goal
            }
        }
        goals.remove(closestGoal)
        total += closestDistance
    }
    return total
}

private fun player() {
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

    printBoard(board)

    val data = Data(playerCoordinates, boxCoordinates, goalCoordinates, board)

    do {
        makeMove(inputMove(), data)
        printBoard(data.board)
    } while (!checkWin(data))

    println("Win!")
}

private fun checkWin(data: Data): Boolean {
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

private fun printBoard(board: ArrayList<ArrayList<Tile>>) {
    for (row in board) {
        row.forEach { it.printChar() }
        println()
    }
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
}


data class Data(var player:Pair<Int,Int>, var box: ArrayList<Pair<Int, Int>>, val goal: ArrayList<Pair<Int, Int>>,
                var board: ArrayList<ArrayList<Tile>>) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Data)
            return false
        if (player.equals(other.player) && box.equals(other.box) && goal.equals(other.goal) && board.equals(other.board))
            return true
        return false
    }

    @Suppress("UNCHECKED_CAST")
    fun copy(): Data {
        val board = ArrayList<ArrayList<Tile>>()
        this.board.forEach {
            val line = ArrayList<Tile>()
            it.forEach{
                line.add(it.copy())
            }
            board.add(line)
        }
        return Data(
            player.copy(), box.clone() as ArrayList<Pair<Int, Int>>,
            goal.clone() as ArrayList<Pair<Int, Int>>, board
        )
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + box.hashCode()
        result = 31 * result + goal.hashCode()
        result = 31 * result + board.hashCode()
        return result
    }

    fun updatePlayerPosition(newPlayerPosition: Pair<Int, Int>) {
        board[player.second][player.first].hasPlayer = false
        player = newPlayerPosition
        board[player.second][player.first].hasPlayer = true
    }

    fun updateBoxPosition(newBoxPosition: Pair<Int, Int>, oldBoxPosition: Pair<Int, Int>) {
        board[oldBoxPosition.second][oldBoxPosition.first].hasBox = false
        box.remove(oldBoxPosition)
        box.add(newBoxPosition)
        board[newBoxPosition.second][newBoxPosition.first].hasBox = true
    }
}


data class U(var cost: Int, var solution: ArrayList<Data>)

fun getInitialSolution(data: Data): U = U(0, arrayListOf(data))

fun getRandomNeigh(u: U, data: Data): U {
    val moves = Moves.values()
    var new = u.solution.last().copy()
    do {
        var move = moves[(Math.random() * (moves.size - 1)).roundToInt()].cord
        makeMove(move, new)
    } while (u.solution.last().equals(new))

    var solution = u.solution.clone() as ArrayList<Data>
    solution.add(new)
    return U(u.cost + 1, solution)
}

fun isOptimum(fu: Double, data: Data): Boolean {
    return checkWin(data)
}