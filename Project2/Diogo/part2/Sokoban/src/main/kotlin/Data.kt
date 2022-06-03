data class Data(var player:Pair<Int,Int>, var box: ArrayList<Pair<Int, Int>>, val goal: ArrayList<Pair<Int, Int>>,
                var move: Pair<Int, Int>, var board: ArrayList<ArrayList<Tile>>) {
    override fun equals(other: Any?): Boolean {     // nao e necessario usar a board para o dominio do problema
        if (other == null || other !is Data)
            return false
        if (player == other.player && box == other.box && goal == other.goal)
            return true
        return false
    }

    @Suppress("UNCHECKED_CAST")
    fun copy(): Data {      // cria uma copia do board, sem ter problemas de referencias
        val board = ArrayList<ArrayList<Tile>>()
        this.board.forEach { row ->     // o array clone tinha problemas com as referencias dentro dos arrays internos
            val line = ArrayList<Tile>()
            row.forEach{
                line.add(it.copy())
            }
            board.add(line)
        }

        return Data(        // criacao de um novo data para retornar
            player.copy(), box.clone() as ArrayList<Pair<Int, Int>>,
            goal.clone() as ArrayList<Pair<Int, Int>>, move.copy(),  board
        )
    }

    override fun hashCode(): Int {      // implementacao do hashcode
        var result = player.hashCode()
        result = 31 * result + box.hashCode()
        result = 31 * result + goal.hashCode()
        result = 31 * result + board.hashCode()
        return result
    }

    fun updatePlayerPosition(newPlayerPosition: Pair<Int, Int>) {       // atualizar a posicao do player
        board[player.second][player.first].hasPlayer = false            // alterar as flags para nao casar problemas
        player = newPlayerPosition                                      // no print
        board[player.second][player.first].hasPlayer = true
    }

    fun updateBoxPosition(newBoxPosition: Pair<Int, Int>, oldBoxPosition: Pair<Int, Int>) { // atualizar a posicao da caixa
        board[oldBoxPosition.second][oldBoxPosition.first].hasBox = false       // alterar as flags para nao causar
        box.remove(oldBoxPosition)                                              // erros no print nem no makemove
        box.add(newBoxPosition)                                                 // e necessario remover a posicao antiga
        board[newBoxPosition.second][newBoxPosition.first].hasBox = true        // e adicionar a nova a lista de caixas
    }
}