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
): SAOut {      // adaptacao do algoritmo fornecido em matlab
    var t = 0.0                                     // tempo
    var T = Tmax                                    // temperatura inicial
    var numEvaluations = 0                          // numero de avaliacoes
    var foundOptimum = false
    var u = getInitialSolution(data)                // obter solucao inicial
    var fu = evalFunc(u,data)                       // custo da solucao inicial
    println("Initial cost: $fu")
    ++numEvaluations                                // incrementar o numero de avaliacoes

    while (!foundOptimum) {                         // enquanto nao encontrar a solucao optima
        var i = 0                                   // numero de repeticoes
        while (i < k && !foundOptimum) { // enquanto nao encontrar a solucao optima e nao exceder o numero de repeticoes
            val v = getRandomNeigh(u, data)         // obter um vizinho aleatorio de u
            val fv = evalFunc(v, data)              // custo do vizinho
            ++numEvaluations                        // incrementar o numero de avalicacoes
            var dif = fv - fu                       // diferenca dos custos
            if (sense == Sense.MAXIMIZE) {          // caso o problema seja de maximizacao
                dif = -dif
            }
            if (dif < 0) {                          // se o custo do vizinho for melhor
                u = v                               // atualizar o individuo com o vizinho e o respetivo custo
                fu = fv
            } else {                                // caso o custo seja pior
                val prob = p(fu, fv, T, sense)      // calcular a probabidade de aceitacao de solucoes piores
                if(Math.random() <= prob) {         // se o numero for inferior á probabilidade
                    u = v                           // atualizar o individuo com o vizinho e o respetivo custo
                    fu = fv
                }
            }
            ++i                                     // incrementar o total de repeticoes
            if (isOptimum(fu, data)) {              // verificar se a solucao e optima
                foundOptimum = true                 // assionar a flag de solucao optima
            }
        }
        if (!foundOptimum) {                        // se nao for a solucao optima
            ++t                                     // incrementar o tempo
            T = Temp(t,Tmax, R)                     // atualizar a temperatura
            if (T < Tmin) {                         // parar caso a temperatura seja menor que a temperatura minima
                break
            }
        }
    }
    println("BestCost: $fu")
    println("numEvaluations: $numEvaluations")
    return SAOut(T, numEvaluations, fu, Tmax, Tmin, R, k, u.solution, u)
}

fun Temp(t: Double, Tmax: Double, R: Double): Double =Tmax * exp(-R * t)    // funcao que calcula a temperatura

fun p(fu: Double, fv: Double,T: Double, sense: Sense): Double {                // funcao exponencial
    return if (sense == Sense.MAXIMIZE)  exp((fv-fu) / (T*fu)) else exp((fu-fv) / (T*fu))
}

// objeto de retorno da funcao SA (de acordo com o codigo em matlab)
data class SAOut(var T: Double, var NumEvaluations: Int, var Cost: Double, var Tmax: Double, var Tmin: Double,
                 var R: Double, var k: Int, var u: ArrayList<Data>, var s: U
)

// objeto que contem o custo e a lista com todos os estados gerados
data class U(var cost: Int, var solution: ArrayList<Data>)

fun getInitialSolution(data: Data): U = U(0, arrayListOf(data))         // funcao geradora de estado inicial

fun evalFunc(u: U, data: Data): Double {
    val tmp = u.solution.last()                             // obter ultimo estado do tabuleiro
    val goals = tmp.goal.clone() as ArrayList<Pair<Int, Int>>
    var total = 0.0
    var playerDistance = Int.MAX_VALUE                      // iniciar a distancia como maxima
    tmp.box.forEach {                                       // calcular a distancia do player a caixa mais proxima
        val distance = Math.abs(it.first - tmp.player.first) + Math.abs(it.second - tmp.player.second)
        if (distance < playerDistance)
            playerDistance = distance
    }
    tmp.box.forEach {                                       // calcular a distancia da caixa ao objetivo mais proximo
        var closestGoal = goals.first()                     // primeiro objetivo (por simlificacao de codigo)
        var closestDistance = Int.MAX_VALUE                 // iniciar a distancia como maxima
        for (goal in goals) {                               // iterar pelos objetivos para encontrar o mais proximo
            val distance = Math.abs(it.first - goal.first) + Math.abs(it.second - goal.second)
            if (distance < closestDistance) {
                closestDistance = distance
                closestGoal = goal
            }
        }
        goals.remove(closestGoal)                           // remover o objetivo mais proximo para nao repetir objetivos
        total += closestDistance                            // incrementar a distancia total
    }
    return 500*total + 100*playerDistance + 0.1*u.solution.size     // retornar o custo
}

fun getRandomNeigh(u: U, data: Data): U {                   // gera um vizinho de forma aleatoria
    val moves = Moves.values()                              // obter o array de movimentos para permitir a indexacao
    val new = u.solution.last().copy()                      // criacao de uma copia do ultimo estado
    var solution = u.solution.clone() as ArrayList<Data>    // criar a solucao do vizinho

    do {                                    // gerar um novo movimento ate gerar um estado diferente do ultimo estado
        val move = moves[(Math.random() * (moves.size - 2)).roundToInt()].cord
        makeMove(move, new)
    } while (u.solution.last() == new)
    solution.add(new)                                       // adicionar o novo estado a solucao

    if (isStuck(new)) {                                     // verificar se o novo estado tem alguma caixa bloqueada
        var last = solution.last()                          // obter o ultimo estado
        val stuckBox = last.box.last()                      // obter a caixa bloqueada
        var fallback = true                                 // flag de auxilio
        while (fallback && last != solution.first()) {  // enquanto a flag estiver ativa e nao estiver no estado inicial
            solution.remove(last)                           // remover o ultimo estado
            last = solution.last()                          // atualizar o ultimo estado com o ultimo estado da solucao
            fallback = false                                // desativar a flag auxiliar
            last.box.forEach {                       // verificar se ainda existe alguma caixa que possa ficar bloqueada
                if (stuckBox.first == it.first || stuckBox.second == it.second)
                    fallback = true                         // assionar a flag caso este se verifique
            }
        }
    }
    return U(solution.size - 1, solution)               // retornar o vizinho
}

fun isStuck(data: Data): Boolean {                          // indica se existe alguma caixa bloqueada
    val moves = Moves.values()                              // obter o array de movimentos para permitir a indexacao
    val size = moves.size - 1                               // numero total de movimentos execuiveis
    var idx = 0                                             // indice auxiliar
    data.box.forEach {                                      // obter o indice
        for (move in moves)
            if(move.cord == data.move) {
                idx = move.ordinal
                break
            }
        var next = idx + 1                                  // calculo dos indices dos vizinhos
        var prev = idx - 1
        if (next == size) next = 0                          // corrigir os indices (se fora dos limites do array)
        if (prev == -1) prev = size - 1
        // se a tile em frente a caixa e uma das laterais(de acordo com o vetor da ultima jogada) e uma parede
        // entao a caixa esta bloqueada
        if (data.board[it.second + moves[idx].cord.second][it.first + moves[idx].cord.first].type == WALL &&
            (data.board[it.second + moves[prev].cord.second][it.first + moves[prev].cord.first].type == WALL ||
                    data.board[it.second + moves[next].cord.second][it.first + moves[next].cord.first].type == WALL))
            return true
    }
    return false
}

fun isOptimum(fu: Double, data: Data): Boolean {        // se a solucao e optima
    return fu <= 0.1
}