import Moves.*
import TileType.*
import ga.*
import sa.*
import java.util.*
import kotlin.collections.ArrayList
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
    board[playerCoordinates.second][playerCoordinates.first].hasPlayer = true
    boxCoordinates.forEach { board[it.second][it.first].hasBox = true }

    val data = Data(playerCoordinates, boxCoordinates, goalCoordinates, INVALID.cord, board)

    while (true) {
        println("choose game mode(by number):")
        println("+---+------------+")
        println("| 1 |   player   |")
        println("+---+------------+")
        println("| 2 |   ia sa    |")
        println("+---+------------+")
        println("| 3 |   ia ga    |")
        println("+---+------------+")
        when (readln()) {
            "1" -> player(data)
            "2" -> iaSa(data)
            "3" -> iaGa(data)
            else -> continue
        }
        break
    }
}

fun iaSa(data: Data) {
    val solution = sa(1.0, 1e-7, 1e-3, 5, data, ::getInitialSolution, ::getRandomNeigh,
        ::evalFunc, ::isOptimum, Sense.MINIMIZE)
    println("Mostrar animacao?")
    val anser = readln().lowercase(Locale.getDefault())
    if (anser == "y" || anser == "yes") {
        optimiseSolution(solution.u).forEach {
            printBoard(it.board)
            Thread.sleep(500)
        }
    } else {
        printBoard(solution.u.last().board)
        println("Total moves: ${solution.s.cost}")
    }
}

fun iaGa(data: Data) {
    fun cross(data: Data, pop: Array<Gen>, crossProb: Double): Array<Gen> {
        val popsize = pop.size
        val randIdx = Array(popsize/2) { Math.random()}
        for ((k, i) in (0 until pop.size-1 step 2).withIndex()) {
            if (randIdx[k] < crossProb) {
                val size = pop[i].size
                val crossPoint = (Math.random()*(size - 1)).roundToInt()
                var tmp: Int
                val i1 = i + 1
                for (j in crossPoint until size) {
                    tmp = pop[i].geneticCode[j]
                    pop[i].geneticCode[j] = pop[i1].geneticCode[j]
                    pop[i1].geneticCode[j] = tmp
                }
            }
        }
        return pop
    }

    fun select(pop: Array<Gen>, popFit: Array<Double>): Array<Gen> {
        val size = pop.size
        val popIds = Array(size) {it}
        for(i in 0 until size) {
            val id = (Math.random() * (size-1)).roundToInt()
            val tmp = popIds[id]
            popIds[id] = popIds[i]
            popIds[i] = tmp
        }
        for (i in pop.indices)
            if(popFit[popIds[i]] < popFit[i])
                pop[i] = pop[popIds[i]]
        return pop
    }

    fun mutable(data: Data, pop: Array<Gen>, mutProb: Double): Array<Gen> {
        pop.forEach {
            if (Math.random() < mutProb)
                for (i in it.geneticCode.indices) {
                    if (Math.random() < mutProb * 2) {
                        val old = it.geneticCode[i]
                        var new = (Math.random() * 3).roundToInt()
                        while(old == new)
                            new = (Math.random() * 3).roundToInt()
                        it.geneticCode[i] = new
                    }
                }
        }
        return pop
    }

    val solution = ga(data, 1_000, 25, .7, .05,::select, ::cross,::mutable,
        ::getInitialSolution, ::evalFunc, ::isOptimum, Sense.MINIMIZE)
    println("Mostrar animacao?")
    val answer = readln().lowercase(Locale.getDefault())
    if (answer == "y" || answer == "yes") {
        optimiseSolution(solution.s).forEach {
            printBoard(it.board)
            Thread.sleep(500)
        }
    } else {
        printBoard(solution.s.last().board)
        println("Total moves: ${solution.s.size}")
    }


}

private fun player(data: Data) {
    fun inputMove(): Pair<Int, Int> {
        return when(readln()) {
            "8" -> UP
            "2" -> DOWN
            "4" -> LEFT
            "6" -> RIGHT
            else -> INVALID
        }.cord
    }
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
        if (solution[i] == solution[i+1])
            solution.removeAt(i)
        else if (solution[i].move == -solution[i + 1].move && solution[i].box == solution[i + 1].box) {
            solution.removeAt(i+1)
            solution.removeAt(i)
            if (i > 0) --i
        } else if (solution[i].move == -solution[i + 2].move && solution[i].move != solution[i + 1].move
            && solution[i].box == solution[i + 2].box) {
            solution.removeAt(i+2)
            solution.removeAt(i)
            if (i > 0) --i
        } else
            ++i
    }
    return solution
}

