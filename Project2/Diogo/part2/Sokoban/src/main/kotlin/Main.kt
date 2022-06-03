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
)   // tabuleiro apenas com as paredes e objetivos (objetos não moviveis)

var playerCoordinates = Pair(11, 8)             // coordenadas do player
var boxCoordinates = arrayListOf(Pair(5, 7))    // lista das coordenadas das caixas
val goalCoordinates = arrayListOf(Pair(17, 7))  // lista das coordenadas dos objetivos

fun main() {
    board[playerCoordinates.second][playerCoordinates.first].hasPlayer = true   // assinalar a flag o player na board
    boxCoordinates.forEach { board[it.second][it.first].hasBox = true }         // assinalar a flag das caixas na board

    // estrutura de dados que guarda o estado do tabuleiro
    val data = Data(playerCoordinates, boxCoordinates, goalCoordinates, INVALID.cord, board)


    while (true) {                  // apresentar o menu
        println("choose game mode(by number):")
        println("+---+------------+")
        println("| 1 |   player   |")
        println("+---+------------+")
        println("| 2 |   ia sa    |")
        println("+---+------------+")
        println("| 3 |   ia ga    |")
        println("+---+------------+")
        when (readln()) {           // obter a escolha do utilizador
            "1" -> player(data)     // modo de jogo
            "2" -> iaSa(data)       // IA algoritmo SA(Simulated Annealing)
            "3" -> iaGa(data)       // IA algoritmo GA(Genetic Algorithm)
            else -> continue        // caso não seja uma opcao valida repetir a pergunta
        }
        break   // caso execute uma escolha
    }
}

fun iaSa(data: Data) {              // IA algoritmo SA(Simulated Annealing)
    val solution = sa(1.0, 1e-7, 1e-3, 5, data, ::getInitialSolution, ::getRandomNeigh,
        ::evalFunc, ::isOptimum, Sense.MINIMIZE)
    println("Mostrar animacao?")
    val anser = readln().lowercase(Locale.getDefault())     // obter a resposta do utilizador
    if (anser == "y" || anser == "yes") {                   // se a resposta for a esperada mostrar a animacao
        optimiseSolution(solution.u).forEach {
            printBoard(it.board)                            // mostra o tabuleiro
            Thread.sleep(500)
        }
    } else {                                                // caso contrario mostrar o estado final e o numero
        printBoard(solution.u.last().board)                 // total de movimentos
        println("Total moves: ${solution.s.cost}")
    }
}

fun iaGa(data: Data) {              // IA algoritmo GA(Genetic Algorithm)
    fun cross(data: Data, pop: Array<Gen>, crossProb: Double): Array<Gen> {     // crossover
        val popsize = pop.size                                      // tamanho da populacao
        val randIdx = Array(popsize/2) { Math.random() }        // array de propriedades de crossover
        for ((k, i) in (0 until pop.size-1 step 2).withIndex()) {   // ++k, i+=2
            if (randIdx[k] < crossProb) {
                val size = pop[i].size                              // tamanho do codigo genetico
                val crossPoint = (Math.random()*(size - 1)).roundToInt()
                var tmp: Int
                val i1 = i + 1                              // indice do vizinho
                for (j in crossPoint until size) {          // dar swap ao codigo genetico a partir de crossPoint
                    tmp = pop[i].geneticCode[j]
                    pop[i].geneticCode[j] = pop[i1].geneticCode[j]
                    pop[i1].geneticCode[j] = tmp
                }
            }
        }
        return pop              // retorna a populacao apos o crossover
    }

    fun select(pop: Array<Gen>, popFit: Array<Double>): Array<Gen> {            // torneio binario
        val size = pop.size                         // tamanho da populacao
        val popIds = Array(size) {it}               // criar um array de indices
        popIds.shuffle()                            // baralhar os indices
        for (i in pop.indices)
            if(popFit[popIds[i]] < popFit[i])       // caso o indice aleatorio seja melhor substitui
                pop[i] = pop[popIds[i]]
        return pop              // retorna a populacao apos o torneio binario
    }

    fun mutable(data: Data, pop: Array<Gen>, mutProb: Double): Array<Gen> {     // mutacao
        pop.forEach {
            if (Math.random() < mutProb)                // verificar se e para mutar
                for (i in it.geneticCode.indices) {
                    if (Math.random() < mutProb * 2) {
                        val old = it.geneticCode[i]     // mutar o codigo genetico
                        var new = (Math.random() * 3).roundToInt()
                        while(old == new)               // enquanto nao mutar
                            new = (Math.random() * 3).roundToInt()
                        it.geneticCode[i] = new
                    }
                }
        }
        return pop              // retorna a populacao apos o mutacao
    }

    val solution = ga(data, 1_000, 25, .7, .05,::select, ::cross,::mutable,
        ::getInitialSolution, ::evalFunc, ::isOptimum, Sense.MINIMIZE)
    println("Mostrar animacao?")
    val anser = readln().lowercase(Locale.getDefault())     // obter a resposta do utilizador
    if (anser == "y" || anser == "yes") {                   // se a resposta for a esperada mostrar a animacao
        optimiseSolution(solution.s).forEach {
            printBoard(it.board)                            // mostra o tabuleiro
            Thread.sleep(500)
        }
    } else {                                                // caso contrario mostrar o estado final e o numero
        printBoard(solution.s.last().board)                 // total de movimentos
        println("Total moves: ${solution.s.size}")
    }
}

private fun player(data: Data) {
    fun inputMove(): Pair<Int, Int> {   // metodo de input
        return when(readln()) {         // le a input do utilizador
            "8" -> UP
            "2" -> DOWN
            "4" -> LEFT
            "6" -> RIGHT
            else -> INVALID
        }.cord                          // retorna o vetor correspondente
    }

    printBoard(board)                   // mostra a board inicial

    do {                                // enquanto nao existir vitoria
        makeMove(inputMove(), data)     // recebe o vetor e faz a jogada, atualizando o data
        printBoard(data.board)          // mostra a board atualizada
    } while (!checkWin(data))

    println("Win!")
}

fun checkWin(data: Data): Boolean {     // verifica se as caixas estao nos objetivos
    data.box.forEach { if(!data.goal.contains(it)) return false}
    return true
}

fun makeMove(move: Pair<Int, Int>, data: Data) {
    // nova posicao do player
    val newPlayerPosition = Pair(data.player.first + move.first, data.player.second + move.second)

    // se existir uma caixa na posicao nova posicao do player
    if (data.board[newPlayerPosition.second][newPlayerPosition.first].hasBox) {
        // nova posicao da caixa
        val newBoxPosition = Pair(newPlayerPosition.first + move.first, newPlayerPosition.second + move.second)
        val boardTile = data.board[newBoxPosition.second][newBoxPosition.first]
        if (boardTile.type != WALL && !boardTile.hasBox) {              // se não existir nada na nova posicao da caixa
            data.updateBoxPosition(newBoxPosition, newPlayerPosition)   // atualizar a posicao da caixa
            data.updatePlayerPosition(newPlayerPosition)                // atualizar a posicao do player
        }
    } else if (data.board[newPlayerPosition.second][newPlayerPosition.first].type != WALL) {    // caso nao tenha caixa
        data.updatePlayerPosition(newPlayerPosition)                    // atualizar a posicao do player
    }
    data.move = move        // atualizar o movimento
}

private fun printBoard(board: ArrayList<ArrayList<Tile>>) {     // printar a board
    for (row in board) {
        row.forEach { it.printChar() }      // printar o caracter da tile
        println()                           // mudar de linha
    }
}

fun optimiseSolution(input: ArrayList<Data>): ArrayList<Data> {     // optimiza a solucao
    val solution = input.clone() as ArrayList<Data>     // cria uma copia para nao estragar o array original
    var i = 0
    while (i != solution.size - 2) {            // executar ate chegar a ultima comparacao
        if (solution[i] == solution[i+1])
            solution.removeAt(i)                // remover um estado se os estados seguidos forem iguais
        else if (solution[i].move == -solution[i + 1].move && solution[i].box == solution[i + 1].box) {
            solution.removeAt(i+1)        // remover um movimentos que se anulam e que nao movam a caixa
            solution.removeAt(i)
            if (i > 0) --i          // decrementar o contador pois pode causar mais remocoes
        } else if (solution[i].move == -solution[i + 2].move && solution[i].move != solution[i + 1].move
            && solution[i].box == solution[i + 2].box) {
            solution.removeAt(i+2)
            solution.removeAt(i)
            if (i > 0) --i          // decrementar o contador pois pode causar mais remocoes
        } else
            ++i                     // caso nao exista alteracao incrementar  indice
    }
    return solution                 // retornar solucao otimizada
}

