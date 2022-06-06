data class State(var board: ArrayList<ArrayList<Tile>>,
                 var path:ArrayList<Pair<Int,Int>>,
                 var playerPos : Pair<Int, Int>,
                 var boxPos : Pair<Int, Int>,
                 val goalPos : Pair<Int, Int>){


     fun moveBox(newBoxPos: Pair<Int,Int>): Boolean{
        if(board[newBoxPos.second][newBoxPos.first].type == TileType.EMPTY_SPACE){
            board[boxPos.second][boxPos.first].hasBox = false;
            board[newBoxPos.second][newBoxPos.first].hasBox = true;
            boxPos = newBoxPos
            return true
        }
        return false
    }

     fun movePlayer(newPlayerPos: Pair<Int,Int>) : Boolean{
        val type = board[newPlayerPos.second][newPlayerPos.first].type
        if(type == TileType.EMPTY_SPACE || board[newPlayerPos.second][newPlayerPos.first].hasBox){
            board[playerPos.second][playerPos.first].hasPlayer = false;
            board[newPlayerPos.second][newPlayerPos.first].hasPlayer = true;
            playerPos = newPlayerPos
            return true
        }
        return false
    }
}

