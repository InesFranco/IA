data class Data(var player:Pair<Int,Int>, var box: ArrayList<Pair<Int, Int>>, val goal: ArrayList<Pair<Int, Int>>,
                var move: Pair<Int, Int>, var board: ArrayList<ArrayList<Tile>>) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Data)
            return false
        if (player == other.player && box == other.box && goal == other.goal)
            return true
        return false
    }

    @Suppress("UNCHECKED_CAST")
    fun copy(): Data {
        val board = ArrayList<ArrayList<Tile>>()
        this.board.forEach { row ->
            val line = ArrayList<Tile>()
            row.forEach{
                line.add(it.copy())
            }
            board.add(line)
        }

        return Data(
            player.copy(), box.clone() as ArrayList<Pair<Int, Int>>,
            goal.clone() as ArrayList<Pair<Int, Int>>, move.copy(),  board
        )
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + box.hashCode()
        result = 31 * result + goal.hashCode()
        result = 31 * result + board.hashCode()
        return result
    }

    fun updatePlayerPosition(newPlayerPosition: Pair<Int, Int>) {
        board[player.second][player.first].hasPlayer = false
        player = newPlayerPosition
        board[player.second][player.first].hasPlayer = true
    }

    fun updateBoxPosition(newBoxPosition: Pair<Int, Int>, oldBoxPosition: Pair<Int, Int>) {
        board[oldBoxPosition.second][oldBoxPosition.first].hasBox = false
        box.remove(oldBoxPosition)
        box.add(newBoxPosition)
        board[newBoxPosition.second][newBoxPosition.first].hasBox = true
    }
}