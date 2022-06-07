import kotlin.math.abs
import kotlin.math.exp
import kotlin.random.Random


fun SA(Tmax: Double, Tmin:Double, R: Double, k: Int, data: State) : State? {
    var t = 0.0     //Rate increment variable
    var T = Tmax    //Step 1  Make T = Tmax and
    var numEvaluations = 1  //Number of evaluations
    var foundOptimum = false    //Variable used to specify stop criteria
    var solution = data
    var fu = evalFunc(solution);
    var z = 2;

    while (!foundOptimum){
        var i = 0;
        while (i < k && !foundOptimum){
            var neighbor = getRandomNeigh(solution)    //Select a neighbor of u, say v.

            var fv = evalFunc(neighbor)                 //Evaluate v

            numEvaluations += 1                         //Increment number of evaluations

            val dif = fv-fu;

            if (dif < 0){
                solution = neighbor;
                printBoard(solution)
                fu = fv;
            }

            else{
                val prob = p(fu, fv, T)
                val x = myRand()
                if (x <= prob ){
                    solution = neighbor;
                    printBoard(solution)
                    fu = fv;
                }
            }


            i += 1;
            z += 1;

            //if optimum found then stop.
            if (isOptimum(solution)){
                println("Finished with $numEvaluations iterations")
                return solution
            }

        }

        if (!foundOptimum){
            //Step 3  Make t = t+1; Set T = T(t)
            //see Eq.(4) of lecture notes
            t +=  1;
            T = newtemp(t, Tmax, R);
            //If  T < Tmin  Stop.
            if (T < Tmin)
                break;
        }
    }
        return null
}


/////////////////////////////////////////////////////

fun evalFunc(state : State): Float {
    return evaluatePath(state)
}


/**
 * If path is invalid(aka hits a wall or if the box becomes stuck)
 * return infinity to cut the probability of the algorithm choosing this state
 * if it's a valid path return the manhattan distance between the box and the player + the distance between box and goal
 * modify the value to prioritize smaller path sizes
 */
fun evaluatePath(state: State): Float{
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second

    for (i in state.path){
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if(state.board[newPlayerPosY][newPlayerPosX].type == TileType.WALL)
            return Float.POSITIVE_INFINITY
        if(newPlayerPosX == newBoxPosX && newPlayerPosY == newBoxPosY){
            newBoxPosX += i.first
            newBoxPosY += i.second

            //if cant move the box return penalty
            if(state.board[newBoxPosY][newBoxPosX].type == TileType.WALL || isStuck(state, Pair(newBoxPosX, newBoxPosY)))
                return Float.POSITIVE_INFINITY
        }
    }
    //Give slight disadvantage to longer paths
    return (0.1f*state.path.size)+(abs(newPlayerPosX-newBoxPosX) + abs(newPlayerPosY-newBoxPosY)).toFloat()
            + abs(state.goalPos.first-newBoxPosX) + abs(state.goalPos.second-newBoxPosY)
}

fun isStuck(state: State, boxPos: Pair<Int, Int>): Boolean {
    if((state.board[boxPos.second+1][boxPos.first].type == TileType.WALL
        || state.board[boxPos.second-1][boxPos.first].type == TileType.WALL)
        &&
        (state.board[boxPos.second][boxPos.first-1].type == TileType.WALL
        || state.board[boxPos.second][boxPos.first+1].type == TileType.WALL)
    )
        return true

    return false
}

fun getRandomNeigh(state: State): State {
    val randomOp = Operation.values()[Random.nextInt(Operation.values().size)]
    var move = Moves.values()[Random.nextInt(0, 4)]                       //randomly choose a move
    var operation = Instruction(randomOp, move.cord)
    return alterPath(state, operation)
}

fun alterPath(state:State, operation : Instruction): State{
    var newState = state.copy()

    if(state.path.size == 0){
        newState.path.add(operation.move)
    }
    else{
        var randomIndex = Random.nextInt(0, newState.path.size)
        when (operation.operation) {
            Operation.ADD -> newState.path.add(newState.path.size, operation.move)
            Operation.REMOVE -> newState.path.removeAt(randomIndex)
            Operation.ALTER -> newState.path = ArrayList(newState.path.subList(randomIndex, newState.path.size))
        }
    }
    return newState
}

private fun printBoard(state:State) {
    val stateAfterRun = runPath(state)
    for (row in stateAfterRun.board) {
        row.forEach { it.printChar() }
        println()
    }
}

fun isOptimum(state:State):Boolean{
    var stateAfterRun = runPath(state)
    return stateAfterRun.boxPos.first == stateAfterRun.goalPos.first && stateAfterRun.boxPos.second == stateAfterRun.goalPos.second
}

fun runPath(state:State):State {
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second

    for (i in state.path) {
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if (newPlayerPosX == newBoxPosX && newPlayerPosY == newBoxPosY) {
            newBoxPosX += i.first
            newBoxPosY += i.second
        }
    }
    var stateAfterRun = state.copy()
    stateAfterRun.updateBoxPosition(Pair(newBoxPosX, newBoxPosY))
    stateAfterRun.updatePlayerPosition(Pair(newPlayerPosX, newPlayerPosY))
    return stateAfterRun
}

//Temperature actualization
fun newtemp (t: Double, Tmax:Double, R:Double) : Double{
    //Outra hipotese: decrescimento quadratico
    // R = ]0,1[, e.g., R = 0.99 (mais lento) ou 0.1 (mais rapido)
    // newTemp = R*Tmax;
    return Tmax * exp(-R * t)
}

//Random number generator
fun myRand() : Double {
    return Math.random()
}


//Probability function
fun p(fu: Float, fv: Float, T:Double):Float{
    return Math.exp((fu-fv) / (T*fu)).toFloat()
}

