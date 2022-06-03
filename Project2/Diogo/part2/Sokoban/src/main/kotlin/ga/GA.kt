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
): GAOut {      // adaptacao do algoritmo fornecido em matlab
    var numEvaluations = popSize                // numero de avaliacoes
    var foundOptimum = false
    var pop = getInitialPopulation(data, popSize, getInitialSolution)   // obter populacao inicial
    var popFit = evaluatePopulation(data, pop, evalFunc)                // obter o fit da populacao

    val bestCount = if (popSize/10 > 0) popSize/10 else 1       // contagem de melhores individuos (10% da populacao)
    var fuI = getBestFitness(popFit, sense)     // obter o melhor individuo (contem o fit e o indice na populacao)
    var best = pop[fuI.i].copy()                // necessario para nao perder o conteudo durante o processo de evolucao

    var t = 0
    while (t < Tmax && !foundOptimum) {
        ++t
        pop = select(pop, popFit)               // torneio binario
        pop = cross(data, pop, crossProb)       // crossover
        pop = mutable(data, pop, mutProb)       // mutacao
        pop.forEach {                           // atualizar a solucao de cada elemento da populacao
            it.solution = updateSolution(data, it.geneticCode)
        }

        for (i in 0 until bestCount)            // elitismo que clona 10% da populacao (melhor elemento)
            pop[i] = best.copy()

        popFit = evaluatePopulation(data, pop, evalFunc)        // obter o fit da populacao (avaliacao)

        numEvaluations += popSize               // incrementar o numero de avaliacoes
        fuI = getBestFitness(popFit, sense)
        best = pop[fuI.i].copy()                // necessario para nao perder o conteudo durante o processo de evolucao

        if(isOptimum(fuI.fit, data)) {
            foundOptimum = true
        }
    }

    fuI = getBestFitness(popFit, sense)         // obter o melhor individuo (contem o fit e o indice na populacao)

    val u = pop[fuI.i]                          // obter o individuo mais apto
    println("BestCost: ${fuI.fit}")
    println("numevaluations: $numEvaluations")

    return GAOut(numEvaluations, fuI.fit, Tmax, popSize, crossProb, mutProb, u, u.solution)
}

fun getBestFitness(popFit: Array<Double>, sense: Sense): FitI {     // seleciona se e para maximizar ou minimizar
    if (sense == Sense.MAXIMIZE)
        return max(popFit)
    return min(popFit)
}
fun getInitialPopulation(data: Data, popSize: Int, getInitialSolution: (data: Data) -> Gen): Array<Gen> {
    return Array(popSize) { getInitialSolution(data) }              // gera a populacao inicial
}

fun evaluatePopulation(data: Data, population: Array<Gen>, evalFunc: (u: Gen, data: Data) -> Double): Array<Double> {
    return Array(population.size) {evalFunc(population[it], data)}  // aplica a funcao de custo a toda a populacao
}

fun max(popFit: Array<Double>): FitI {      // retorna o indice e fit do individuo com maior fit
    var max = popFit[0]
    var maxIdx = 0
    for (i in 1 until popFit.size) {
        if (popFit[i] > max) {              // se o fit atual foi maior que max atualizar o indice e o max
            max = popFit[i]
            maxIdx = i
        }
    }
    return FitI(max, maxIdx)
}

fun min(popFit: Array<Double>): FitI {      // retorna o indice e fit do individuo com menor fit
    var min = popFit[0]
    var minIdx = 0
    for (i in 1 until popFit.size) {        // se o fit atual foi menor que min atualizar o indice e o min
        if (popFit[i] < min) {
            min = popFit[i]
            minIdx = i
        }
    }
    return FitI(min, minIdx)
}

fun getInitialSolution(data: Data) = Gen(500, data)     // gera um individuo com um codigo genetico aleatorio

fun evalFunc(gen: Gen, data: Data): Double {
    val tmp = gen.solution.last()                           // obter ultimo estado do tabuleiro
    val goals = tmp.goal.clone() as ArrayList<Pair<Int, Int>>
    var total = 0.0
    var playerDistance = Int.MAX_VALUE                      // iniciar a distancia como maxima
    tmp.box.forEach {                                       // calcular a distancia do player a caixa mais proxima
        val distance = abs(it.first - tmp.player.first) + abs(it.second - tmp.player.second)
        if (distance < playerDistance)
            playerDistance = distance
    }
    tmp.box.forEach {                                       // calcular a distancia da caixa ao objetivo mais proximo
        var closestGoal = goals.first()                     // primeiro objetivo (por simlificacao de codigo)
        var closestDistance = Int.MAX_VALUE                 // iniciar a distancia como maxima
        for (goal in goals) {                               // iterar pelos objetivos para encontrar o mais proximo
            val distance = abs(it.first - goal.first) + abs(it.second - goal.second)
            if (distance < closestDistance) {
                closestDistance = distance
                closestGoal = goal
            }
        }
        goals.remove(closestGoal)                           // remover o objetivo mais proximo para nao repetir objetivos
        total += closestDistance                            // incrementar a distancia total
    }
    return 500*total + 100*playerDistance + 0.1*gen.solution.size   // retornar o custo
}

data class FitI(var fit: Double, var i: Int)                // objeto para guardar o fit e o indice do individuo

// objeto de retorno da funcao GA (de acordo com o codigo em matlab)
data class GAOut(val numEvaluations: Int, val cost: Double, val Tmax: Int, val popSize: Int, val crossProb: Double,
                 val mutProb: Double, val u: Gen, val s: ArrayList<Data>)

// objeto que representa o individuo, sendo geneticCode o codigo genetico, size o tamanho do codigo genetido e
// data que contem a lista dos estados do tabuleiro gerados pelo codigo genetico do individuo
data class Gen(var geneticCode: Array<Int>, val size: Int,var solution: ArrayList<Data>) {

    // construtor que gera codigo genetido aleatorio e a solucao para o codigo genetico gerado
    constructor(size: Int, data: Data) : this(Array(size) {(Math.random()*3).roundToInt()}, size, ArrayList()) {
        solution = updateSolution(data, geneticCode)
    }

    // metodo para evitar problemas de referencias no individuo best
    fun copy() = Gen(geneticCode.clone(),size, solution.clone() as ArrayList<Data>)
}

// atualiza a lista dos estados da board
private fun updateSolution(data: Data, geneticCode: Array<Int>): ArrayList<Data> {
    val moves = Moves.values()                  // obter o array com os movimentos para permitir a indexacao
    val solution = arrayListOf(data)            // comecar com a data inicial
    run cycle@{                 // criar uma label para permitir que deixa de gerar estados quando atingir a solucao
        geneticCode.forEach {   // iterar sobre o codigo genetico para gerar o novo estado
            val newData = solution.last().copy()
            makeMove(moves[it].cord, newData)   // utilizar o array de moves para indexar o enumerado
            solution.add(newData)               // adicionar o novo estado a solucao
            if (checkWin(newData))              // varificar se o estado e solucao
                return@cycle
        }
    }
    return solution             // retornar a solucao atualizada
}