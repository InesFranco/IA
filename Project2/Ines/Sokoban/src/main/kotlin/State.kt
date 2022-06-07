data class State(var board: ArrayList<ArrayList<Tile>>,
                 var path:ArrayList<Pair<Int,Int>>,
                 var playerPos : Pair<Int, Int>,
                 var boxPos : Pair<Int, Int>,
                 val goalPos : Pair<Int, Int>){


    @Suppress("UNCHECKED_CAST")
    fun copy(): State {      // cria uma copia do board, sem ter problemas de referencias
        val board = ArrayList<ArrayList<Tile>>()
        this.board.forEach { row ->     // o array clone tinha problemas com as referencias dentro dos arrays internos
            val line = ArrayList<Tile>()
            row.forEach{
                line.add(it.copy())
            }
            board.add(line)
        }
        val path = ArrayList<Pair<Int,Int>>()
        this.path.forEach {     // o array clone tinha problemas com as referencias dentro dos arrays internos
            path.add(it.copy())
        }

        return State(        // criacao de um novo data para retornar
            board, path, playerPos, boxPos, goalPos
        )
    }



    fun updatePlayerPosition(newPlayerPosition: Pair<Int, Int>) {       // atualizar a posicao do player
        board[playerPos.second][playerPos.first].hasPlayer = false            // alterar as flags para nao casar problemas
        playerPos = newPlayerPosition                                      // no print
        board[playerPos.second][playerPos.first].hasPlayer = true
    }

    fun updateBoxPosition(newBoxPosition: Pair<Int, Int>){ // atualizar a posicao da caixa
        board[boxPos.second][boxPos.first].hasBox = false       // alterar as flags para nao causar
        boxPos = newBoxPosition
        board[newBoxPosition.second][newBoxPosition.first].hasBox = true        // e adicionar a nova a lista de caixas
    }

}

