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


fun SA(Tmax: Double, Tmin:Double, R: Double, k: Int, data: State) : State? {
    //Rate increment variable
    var t = 0.0
    //Step 1  Make T = Tmax and
    var T = Tmax
    //Number of evaluations
    var numEvaluations = 0
    //Variable used to specify stop criteria
    var foundOptimum = false

    //Choose a solution u (at random) and compute fu = f(u)
    var solution = getInitialSolution(data)
    printBoard(solution)

    var fu = evalFunc(solution);
    print("Initial cost: $fu");

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
            printBoard(neighbor)

            //Evaluate v
            var fv = evalFunc(neighbor);

            //Increment number of evaluations
            numEvaluations += 1;
            println("num Evaluations : $numEvaluations")

            // If f(v) < f(u) (minimization) make u = v;
            // Else make u = v with probability
            // p = exp((fu-fv)/(fu T)))
            val dif = fv-fu;

            if (dif < 0){
                solution = neighbor;
                fu = fv;
            }

            else{
                val prob = p(fu, fv, T)
                val x = myRand()
                if (x <= prob){
                    //Accept this solution
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
        print("Didn't succeed after  $numEvaluations iterations")
        return null
}


/////////////////////////////////////////////////////

fun getInitialSolution(state:State) : State{
    return state
}

fun evalFunc(state : State): Float {
    //manhattan distance
    var score = evaluatePath(state)

    return score + abs(state.playerPos.first-state.boxPos.first) + abs(state.playerPos.second-state.boxPos.second)
        + abs(state.goalPos.first-state.boxPos.first) + abs(state.goalPos.second-state.boxPos.second)
}

fun evaluatePath(state: State): Float{
    var newPlayerPosX = state.playerPos.first
    var newPlayerPosY = state.playerPos.second
    var newBoxPosX = state.boxPos.first
    var newBoxPosY = state.boxPos.second

    for (i in state.path){
        newPlayerPosX += i.first
        newPlayerPosY += i.second

        if(!movePlayer(state, Pair(newPlayerPosX, newPlayerPosY)))
            return Float.POSITIVE_INFINITY
        if(state.board[newPlayerPosY][newPlayerPosX].type == TileType.BOX){
            newBoxPosX += i.first
            newBoxPosY += i.second

            //if cant move the box return penalty
            if(!moveBox(state, Pair(newBoxPosX, newBoxPosY)))
                return Float.POSITIVE_INFINITY
        }
    }
    return -10f
}

fun getRandomNeigh(state: State): State {
    val moves = arrayOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))       //possible moves

    val randomOp = Operation.values()[Random.nextInt(Operation.values().size)]

    var move = moves[Random.nextInt(0, 4)]                       //randomly choose a move
    var operation = Instruction(randomOp, move)

    return alterPath(state, operation)
}

fun alterPath(state:State, operation : Instruction): State{
    var newState = state.copy()
    var newPath = state.path.map { it.copy() }
    var newBoard = state.board.map { line -> line.map { it.copy() } }
    newBoard[newState.boxPos.second][newState.boxPos.first].hasBox = true
    newBoard[newState.playerPos.second][newState.playerPos.first].hasPlayer = true
    newState.path = newPath as ArrayList<Pair<Int, Int>>
    newState.board = newBoard as ArrayList<ArrayList<Tile>>



    if(state.path.size == 0){
        newState.path.add(operation.move)
    }
    else{
        var randomIndex = Random.nextInt(0, newState.path.size)

        when (operation.operation) {
            Operation.ADD -> newState.path.add(randomIndex, operation.move)
            Operation.REMOVE -> newState.path.removeAt(randomIndex)
            Operation.ALTER -> newState.path[randomIndex] = operation.move
        }
    }
    return newState
}


private fun moveBox(state:State, newBoxPos: Pair<Int,Int>): Boolean{
    if(state.board[newBoxPos.second][newBoxPos.first].type == TileType.EMPTY_SPACE){
        state.boxPos = newBoxPos
        state.board[state.boxPos.second][state.boxPos.first].hasBox = false;
        state.board[newBoxPos.second][newBoxPos.first].hasBox = true;
        return true
    }
    return false
}

private fun movePlayer(state:State, newPlayerPos: Pair<Int,Int>) : Boolean{
    val type = state.board[newPlayerPos.second][newPlayerPos.first].type
    if(type == TileType.EMPTY_SPACE ||type == TileType.BOX){
        state.playerPos = newPlayerPos
        state.board[state.playerPos.second][state.playerPos.first].hasPlayer = false;
        state.board[newPlayerPos.second][newPlayerPos.first].hasPlayer = true;
        return true
    }
    return false
}

private fun printBoard(state:State) {
    for (row in state.board) {
        row.forEach { it.printChar() }
        println()
    }
}

fun isOptimum(state:State):Boolean{
    return state.boxPos.first == state.goalPos.first && state.boxPos.second == state.goalPos.second
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

