package ga

import Data
import Moves
import Sense
import checkWin
import makeMove
import sa.U
import kotlin.math.abs
import kotlin.math.roundToInt

fun ga(data: Data, Tmax: Int, popSize: Int, crossProb: Double, mutProb: Double,
       select: (pop: Array<Gen>, popFit: Array<Double>) -> Array<Gen>,
       cross: (data: Data, pop: Array<Gen>, crossProb: Double) -> Array<Gen>,
       mutable: (data: Data, pop: Array<Gen>, mutProb: Double) -> Array<Gen>,
       getInitialSolution: (data: Data) -> Gen, evalFunc: (u: Gen, data: Data) -> Double,
       isOptimum: (fu: Double, data: Data) -> Boolean, sense: Sense
): GAOut {
    var numEvaluations = popSize
    var foundOptimum = false
    val initialPop = getInitialPopulation(data, popSize, getInitialSolution)
    val initialPopFit = evaluatePopulation(data, initialPop, evalFunc)
    var pop = initialPop
    var popFit = initialPopFit

    val bestCount = if (popSize/10 > 0) popSize/10 else 1
    var fuI = getBestFitness(popFit, sense)
    var best = pop[fuI.i].copy()

    var t = 0
    while (t < Tmax && !foundOptimum) {
        ++t
        pop = select(pop, popFit)
        pop = cross(data, pop, crossProb)
        pop = mutable(data, pop, mutProb)
        pop.forEach {
            it.solution = updateSolution(data, it.geneticCode)
        }

        for (i in 0 until bestCount)
            pop[i] = best.copy()

        popFit = evaluatePopulation(data, pop, evalFunc)

        numEvaluations += popSize
        fuI = getBestFitness(popFit, sense)
        best = pop[fuI.i].copy()

        if(isOptimum(fuI.fit, data)) {
            foundOptimum = true
        }
    }

    fuI = getBestFitness(popFit, sense)

    val u = pop[fuI.i]
    println("BestCost: ${fuI.fit}")
    println("numevaluations: $numEvaluations")

    return GAOut(numEvaluations, fuI.fit, Tmax, popSize, crossProb, mutProb, u, u.solution)
}

fun getBestFitness(popFit: Array<Double>, sense: Sense): FitI {
    if (sense == Sense.MAXIMIZE)
        return max(popFit)
    return min(popFit)
}
fun getInitialPopulation(data: Data, popSize: Int, getInitialSolution: (data: Data) -> Gen): Array<Gen> {
    return Array(popSize) { getInitialSolution(data) }
}

fun evaluatePopulation(data: Data, population: Array<Gen>, evalFunc: (u: Gen, data: Data) -> Double): Array<Double> {
    return Array(population.size) {evalFunc(population[it], data)}
}

fun max(popFit: Array<Double>): FitI {
    var max = popFit[0]
    var maxIdx = 0
    for (i in 1 until popFit.size) {
        if (popFit[i] > max) {
            max = popFit[i]
            maxIdx = i
        }
    }
    return FitI(max, maxIdx)
}

fun min(popFit: Array<Double>): FitI {
    var min = popFit[0]
    var minIdx = 0
    for (i in 1 until popFit.size) {
        if (popFit[i] < min) {
            min = popFit[i]
            minIdx = i
        }
    }
    return FitI(min, minIdx)
}

fun mean(popFit: Array<Double>): Double {
    var total = 0.0
    popFit.forEach {
        total += it
    }
    return total / popFit.size
}

fun getInitialSolution(data: Data) = Gen(500, data)

fun evalFunc(gen: Gen, data: Data): Double {
    val tmp = gen.solution.last()
    val goals = tmp.goal.clone() as ArrayList<Pair<Int, Int>>
    var total = 0.0
    var playerDistance = Int.MAX_VALUE
    tmp.box.forEach {
        val distance = abs(it.first - tmp.player.first) + abs(it.second - tmp.player.second)
        if (distance < playerDistance)
            playerDistance = distance
    }
    tmp.box.forEach {
        var closestGoal = goals.first()
        var closestDistance = Int.MAX_VALUE
        for (goal in goals) {
            val distance = abs(it.first - goal.first) + abs(it.second - goal.second)
            if (distance < closestDistance) {
                closestDistance = distance
                closestGoal = goal
            }
        }
        goals.remove(closestGoal)
        total += closestDistance
    }
    return 500*total + 100*playerDistance + 0.1*gen.solution.size
}

data class FitI(var fit: Double, var i: Int)

data class GAOut(val numEvaluations: Int, val cost: Double, val Tmax: Int, val popSize: Int, val crossProb: Double,
                 val mutProb: Double, val u: Gen, val s: ArrayList<Data>)

data class Gen(var geneticCode: Array<Int>, val size: Int,var solution: ArrayList<Data>) {
    constructor(geneticCode: Array<Int>,solution: ArrayList<Data>) : this(geneticCode, geneticCode.size, solution)

    constructor(size: Int, data: Data) : this(Array(size) {(Math.random()*3).roundToInt()}, size, ArrayList()) {
        solution = updateSolution(data, geneticCode)
    }

    fun copy() = Gen(geneticCode.clone(),size, solution.clone() as ArrayList<Data>)
}

private fun updateSolution(data: Data, geneticCode: Array<Int>): ArrayList<Data> {
    val moves = Moves.values()
    val solution = arrayListOf(data)
    run cycle@{
        geneticCode.forEach {
            val newData = solution.last().copy()
            makeMove(moves[it].cord, newData)
            solution.add(newData)
            if (checkWin(newData))
                return@cycle
        }
    }
    return solution
}