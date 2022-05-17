

enum class Moves(var cord: Pair<Int, Int>) {

    UP(0,-1), DOWN(0, 1), LEFT(-1,0), RIGHT(1, 0),INVALID(0,0);


    constructor(x: Int, y: Int) : this(Pair(x, y))
}