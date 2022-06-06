import TileType.*
import java.util.*
import kotlin.collections.ArrayList
import Moves.*


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

    // State stores the board, the path and the key coordinates
    val data = State(board, ArrayList(), playerCoordinates, boxCoordinates.first(), goalCoordinates.first())


    while (true) {                  // apresentar o menu
        println("choose game mode(by number):")
        println("+---+------------+")
        println("| 1 |   player   |")
        println("+---+------------+")
        println("| 2 |   ia sa    |")
        println("+---+------------+")

        when (readLine()) {         // obter a escolha do utilizador
            "1" -> player(data)     // modo de jogo
            "2" -> iaSa(data)       // IA algoritmo SA(Simulated Annealing)
            else -> continue        // caso não seja uma opcao valida repetir a pergunta
        }
        break   // caso execute uma escolha
    }
}

fun iaSa(data: State) {              // IA algoritmo SA(Simulated Annealing)
    val solution = SA(10.0, 1e-7, 1e-3, 200, data)
    if(solution == null){
        println("Could not find solution")
    }
    else{
        println("Reached end with path with a size of ${solution.path.size}: ")
        printPath(solution)
    }



}

fun printPath(state: State) {
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second

/*
    for (i in state.path){
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if(newPlayerPosY == newBoxPosY && newBoxPosX == newPlayerPosX) {
            newBoxPosX += i.first
            newBoxPosY += i.second
        }

        state.movePlayer(Pair(newPlayerPosX, newPlayerPosY))
        state.moveBox(Pair(newBoxPosX, newBoxPosY))
        printBoard(state.board)
    }
*/
    println("path length ${state.path.size} ")
    state.path.forEach{println(it)}
    print("Finished program")

}


private fun player(state: State) {
    fun inputMove(): Pair<Int, Int> {   // metodo de input
        return when(readLine()) {         // le a input do utilizador
            "8" -> UP
            "2" -> DOWN
            "4" -> LEFT
            "6" -> RIGHT
            else -> INVALID
        }.cord                          // retorna o vetor correspondente
    }

    printBoard(board)                   // mostra a board inicial

    do {                                // enquanto nao existir vitoria
        makeMove(inputMove(), state)     // recebe o vetor e faz a jogada, atualizando o data
        printBoard(state.board)          // mostra a board atualizada
    } while (!checkWin(state))

    println("Win!")
}

fun checkWin(data: State): Boolean {     // verifica se as caixas estao nos objetivos
    return data.boxPos == data.goalPos
}

fun makeMove(move: Pair<Int, Int>, data: State) {
    // nova posicao do player
    val newPlayerPosition = Pair(data.playerPos.first + move.first, data.playerPos.second + move.second)

    // se existir uma caixa na posicao nova posicao do player
    if (data.board[newPlayerPosition.second][newPlayerPosition.first].hasBox) {
        // nova posicao da caixa
        val newBoxPosition = Pair(newPlayerPosition.first + move.first, newPlayerPosition.second + move.second)
        val boardTile = data.board[newBoxPosition.second][newBoxPosition.first]
        if (boardTile.type != WALL && !boardTile.hasBox) {              // se não existir nada na nova posicao da caixa
            data.moveBox(newBoxPosition)   // atualizar a posicao da caixa
            data.movePlayer(newPlayerPosition)                // atualizar a posicao do player
        }
    } else if (data.board[newPlayerPosition.second][newPlayerPosition.first].type != WALL) {    // caso nao tenha caixa
        data.movePlayer(newPlayerPosition)                    // atualizar a posicao do player
    }
}

private fun printBoard(board: ArrayList<ArrayList<Tile>>) {     // printar a board
    for (row in board) {
        row.forEach { it.printChar() }      // printar o caracter da tile
        println()                           // mudar de linha
    }
}



