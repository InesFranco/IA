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
)   // tatubleiro apenas com as paredes e objetivos (objetos não moviveis)

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

        when (readln()) {           // obter a escolha do utilizador
            "1" -> player(data)     // modo de jogo
            "2" -> iaSa(data)       // IA algoritmo SA(Simulated Annealing)
            else -> continue        // caso não seja uma opcao valida repetir a pergunta
        }
        break   // caso execute uma escolha
    }
}

fun iaSa(data: Data) {              // IA algoritmo SA(Simulated Annealing)
    val solution = sa(1.0, 1e-7, 1e-3, 5, data)
    println("Mostrar animacao?")
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

