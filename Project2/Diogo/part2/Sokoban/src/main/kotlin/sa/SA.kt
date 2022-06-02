package sa

import Data
import Moves
import Sense
import kotlin.math.exp
import kotlin.math.roundToInt
import TileType.WALL
import checkWin
import makeMove


fun sa(Tmax: Double, Tmin: Double, R: Double, k: Int, data: Data, getInitialSolution: (data: Data) -> U,
       getRandomNeigh: (u: U, data: Data) -> U, evalFunc: (u: U, data: Data) -> Double,
       isOptimum: (fu: Double, data: Data) -> Boolean, sense: Sense
): SAOut {
    var t = 0.0
    var T = Tmax
    var numEvaluations = 0
    var foundOptimum = false
    var u = getInitialSolution(data)
    var fu = evalFunc(u,data)
    println("Initial cost: $fu")
    ++numEvaluations

    while (!foundOptimum) {
        var i = 0
        while (i < k && !foundOptimum) {
            val v = getRandomNeigh(u, data)
            val fv = evalFunc(v, data)
            ++numEvaluations
            var dif = fv - fu
            if (sense == Sense.MAXIMIZE) {
                dif = -dif
            }
            if (dif < 0) {
                u = v
                fu = fv
            } else {
                val prob = p(fu, fv, T, sense)
                val x = myRand()
                if(x <= prob) {
                    u = v
                    fu = fv
                }
            }
            ++i
            if (isOptimum(fu, data)) {
                foundOptimum = true
            }
        }
        if (!foundOptimum) {
            ++t
            T = Temp(t,Tmax, R)
            if (T < Tmin) {
                break
            }
        }
    }
    println("BestCost: $fu")
    println("numEvaluations: $numEvaluations")
    return SAOut(T, numEvaluations, fu, Tmax, Tmin, R, k, u.solution, u)
}

fun Temp(t: Double, Tmax: Double, R: Double): Double =Tmax * exp(-R * t)

fun myRand(): Double = Math.random()

fun p(fu: Double, fv: Double,T: Double, sense: Sense): Double {
    return if (sense == Sense.MAXIMIZE)  exp((fv-fu) / (T*fu)) else exp((fu-fv) / (T*fu))
}

data class SAOut(var T: Double, var NumEvaluations: Int, var Cost: Double, var Tmax: Double, var Tmin: Double,
                 var R: Double, var k: Int, var u: ArrayList<Data>, var s: U
)


data class U(var cost: Int, var solution: ArrayList<Data>)

fun getInitialSolution(data: Data): U = U(0, arrayListOf(data))

fun evalFunc(u: U, data: Data): Double {
    val tmp = u.solution.last()
    val goals = tmp.goal.clone() as ArrayList<Pair<Int, Int>>
    var total = 0.0
    var playerDistance = Int.MAX_VALUE
    tmp.box.forEach {
        val distance = Math.abs(it.first - tmp.player.first) + Math.abs(it.second - tmp.player.second)
        if (distance < playerDistance)
            playerDistance = distance
    }
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
    return 500*total + 100*playerDistance + 0.1*u.solution.size
}

fun getRandomNeigh(u: U, data: Data): U {
    val moves = Moves.values()
    val new = u.solution.last().copy()
    var solution = u.solution.clone() as ArrayList<Data>

    do {
        val move = moves[(Math.random() * (moves.size - 2)).roundToInt()].cord
        makeMove(move, new)
    } while (u.solution.last() == new)
    solution.add(new)

    if (isStuck(new)) {
        var last = solution.last()
        val stuckBox = last.box.last()
        var fallback = true
        while (fallback && last != solution.first()) {
            solution.remove(last)
            last = solution.last()
            fallback = false
            last.box.forEach {
                if (stuckBox.first == it.first || stuckBox.second == it.second)
                    fallback = true
            }
        }
        solution = arrayListOf(solution.first())
    }
    return U(solution.size - 1, solution)
}

fun isStuck(data: Data): Boolean {
    val moves = Moves.values()
    val size = moves.size - 1
    var idx = 0
    data.box.forEach {
        for (move in moves)
            if(move.cord == data.move) {
                idx = move.ordinal
                break
            }
    }
    data.box.forEach {
        var next = idx + 1
        var prev = idx - 1
        if (next == size) next = 0
        if (prev == -1) prev = size - 1
        if (data.board[it.second + moves[idx].cord.second][it.first + moves[idx].cord.first].type == WALL &&
            (data.board[it.second + moves[prev].cord.second][it.first + moves[prev].cord.first].type == WALL ||
                    data.board[it.second + moves[next].cord.second][it.first + moves[next].cord.first].type == WALL))
            return true
    }
    return false
}

fun isOptimum(fu: Double, data: Data): Boolean {
    return fu <= 0.1
}