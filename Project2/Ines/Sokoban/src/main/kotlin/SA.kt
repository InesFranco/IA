import kotlin.math.abs
import kotlin.math.exp
import kotlin.random.Random


/* Simulated Annealing (algorithm 2 for minimization of f)
%
% Step 1  Make T = Tmax  and Choose a solution u (at random)
%
% Step 2  Select a neighbor of u, say v
%         If f(v) < f(u) make u = v;
%         Else make u = v with probability
%              p = exp((fu-fv)/(fu * T)))
%
%         Repeat Step 2  k times
%
% Step 3  Make t = t+1; Set T = T(t)
%         see Eq.(4) of lecture notes
%
%         If  T >= Tmin  go to Step 2;
%         Else Stop.
%*/
var initialPlayerPos: Pair<Int, Int>? = null
var initialBoxPos: Pair<Int, Int>? = null

fun SA(Tmax: Double, Tmin:Double, R: Double, k: Int, data: State) : State? {
    //Rate increment variable
    var t = 0.0
    //Step 1  Make T = Tmax and
    var T = Tmax
    //Number of evaluations
    var numEvaluations = 0
    //Variable used to specify stop criteria
    var foundOptimum = false
    initialPlayerPos = data.playerPos
    initialBoxPos = data.boxPos

    //Choose a solution u (at random) and compute fu = f(u)
    var solution = getInitialSolution(data)
    printBoard(solution)

    var fu = evalFunc(solution);

    //Increment number of evaluations
    numEvaluations += 1


    var z = 1;
    z += 1;

    while (!foundOptimum){
        /* Step 2  Select a neighbor of u, say v
    %         If f(v) < f(u) make u = v;
    %         Else make u = v with probability
    %              p = exp((fu-fv)/(fu * T)))
    %
    %         Repeat Step 2   k times
    */

        var i = 0;
        while (i < k && !foundOptimum){
            //Select a neighbor of u, say v.
            var neighbor = getRandomNeigh(solution);

            //Evaluate v
            var fv = evalFunc(neighbor);

            //Increment number of evaluations
            numEvaluations += 1;


            // If f(v) < f(u) (minimization) make u = v;
            // Else make u = v with probability
            // p = exp((fu-fv)/(fu T)))
            val dif = fv-fu;

            if (dif < 0){
                solution = neighbor;
                printBoard(solution)
                fu = fv;
            }

            else{
                val prob = p(fu, fv, T)
                val x = myRand()
                if (x <= prob && fv != Float.POSITIVE_INFINITY){
                    //Accept this solution
                    printBoard(solution)
                    solution = neighbor;
                    fu = fv;
                }
            }

            i += 1;
            z += 1;

            //if optimum found then stop.
            if (isOptimum(solution)){
                foundOptimum = true;
                print("Finished with : $numEvaluations iterations")
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
        println("Didn't succeed after  $numEvaluations iterations")
        return null
}


/////////////////////////////////////////////////////

fun getInitialSolution(state:State) : State{
    return state
}

fun evalFunc(state : State): Float {
    return evaluatePath(state)
}

fun evaluatePath(state: State): Float{
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second


    for (i in state.path){
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if(newPlayerPosY == newBoxPosY && newBoxPosX == newPlayerPosX){
            newBoxPosX += i.first
            newBoxPosY += i.second

            //if cant move the box return penalty
            if(board[newBoxPosY][newBoxPosX].type != TileType.EMPTY_SPACE
                && board[newBoxPosY][newBoxPosX].type != TileType.GOAL ){
                return Float.POSITIVE_INFINITY
            }
            if(isBoxStuck(state, Pair(newBoxPosX, newBoxPosY))){
                return Float.POSITIVE_INFINITY
            }
        }
        if(board[newPlayerPosY][newPlayerPosX].type != TileType.EMPTY_SPACE)
            return Float.POSITIVE_INFINITY
    }

    var finalScore = 0.5f*(abs(newPlayerPosX-newBoxPosX) + abs(newPlayerPosY-newBoxPosY)).toFloat()
    + abs(state.goalPos.first-newBoxPosX) + abs(state.goalPos.second-newBoxPosY)

    return finalScore
}

fun isBoxStuck(state: State, boxPos: Pair<Int,Int>): Boolean {
    if((state.board[boxPos.second][boxPos.first-1].type == TileType.WALL
                || state.board[boxPos.second][boxPos.first+1].type == TileType.WALL)
        &&
        (state.board[boxPos.second+1][boxPos.first].type == TileType.WALL
                || state.board[boxPos.second-1][boxPos.first].type == TileType.WALL))
        return true
    return false
}

fun getRandomNeigh(state: State): State {
    val randomOp = Operation.values()[Random.nextInt(Operation.values().size)]
    var move = Moves.values()[Random.nextInt(0, 4)]                       //randomly choose a move
    var operation = Instruction(randomOp, move)

    return alterPath(state, operation)
}

fun alterPath(state:State, instruction : Instruction): State{
    var newState = state.copy()
    var newPath = state.path.map { it.copy() }
    var newBoard = state.board.map { line -> line.map { it.copy() } }
    newState.path = newPath as ArrayList<Pair<Int, Int>>
    newState.board = newBoard as ArrayList<ArrayList<Tile>>

    if(state.path.size == 0){
        newState.path.add(instruction.move.cord)
    }
    else{
        var randomIndex = Random.nextInt(0, newState.path.size)

        when (instruction.operation) {
            Operation.ADD -> newState.path.add(Random.nextInt(0, newState.path.size+1), instruction.move.cord)
            //Operation.REMOVE -> newState.path.removeAt(Random.nextInt(0, newState.path.size))
            Operation.ALTER -> newState.path[Random.nextInt(0, newState.path.size)] = instruction.move.cord
        }
    }
    return newState
}


private fun printBoard(state:State) {
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second


    for (i in state.path){
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if(newPlayerPosY == newBoxPosY && newBoxPosX == newPlayerPosX) {
            newBoxPosX += i.first
            newBoxPosY += i.second
        }
    }
    state.board[state.boxPos.second][state.boxPos.first].hasBox = false
    state.board[state.playerPos.second][state.playerPos.first].hasPlayer = false

    state.board[newPlayerPosY][newPlayerPosX].hasPlayer = true
    state.board[newBoxPosY][newBoxPosX].hasBox = true

    for (row in state.board) {
        row.forEach { it.printChar() }
        println()
    }

    state.board[newPlayerPosY][newPlayerPosX].hasPlayer = false
    state.board[newBoxPosY][newBoxPosX].hasBox = false
    state.board[state.boxPos.second][state.boxPos.first].hasBox = true
    state.board[state.playerPos.second][state.playerPos.first].hasPlayer = true

}

fun isOptimum(state:State):Boolean{
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second


    for (i in state.path){
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if(newPlayerPosY == newBoxPosY && newBoxPosX == newPlayerPosX) {
            newBoxPosX += i.first
            newBoxPosY += i.second
        }
    }

    return newBoxPosX == state.goalPos.first && newBoxPosY == state.goalPos.second
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
fun p(fu: Float, fv: Float, T:Double):Double{
    return Math.exp((fu-fv) / (T*fu))
}

