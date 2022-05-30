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

public fun SA(Tmax: Double, Tmin:Double, R: Double, k: Int, data: State) : State? {
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
            if (isOptimum(data)){
                foundOptimum = true;
                return solution
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
    }
        return null
}


/////////////////////////////////////////////////////

fun getInitialSolution(state:State) : State{
    return state
}

fun evalFunc(state : State): Int {
    //manhattan distance
    return (state.playerPos.first-state.boxPos.first) + (state.playerPos.second-state.boxPos.second)
        + (state.goalPos.first-state.boxPos.first) + (state.goalPos.second-state.boxPos.second)
}

fun getRandomNeigh(state:State) : State{
    val moves = arrayOf(Pair(1,0), Pair(-1,0), Pair(0,1), Pair(0,-1))
    val move = moves[Random.nextInt(0,4)]                       //randomly choose a move

    var newPlayerPosX = state.playerPos.first + move.first
    var newPlayerPosY = state.playerPos.second + move.second
    var newBoxPosX = state.boxPos.first + move.first
    var newBoxPosY = state.boxPos.second + move.second
    var newBoard = state.board

    if(newPlayerPosX == state.boxPos.first && newPlayerPosY  == state.boxPos.second && state.board[newBoxPosY][newBoxPosX].type != TileType.WALL){
        newBoard[state.boxPos.second][state.boxPos.first].hasBox = false;
        newBoard[newBoxPosY][newBoxPosX].hasBox = true;
        newBoard[state.playerPos.second][state.playerPos.first].hasPlayer = false
        newBoard[newPlayerPosY][newPlayerPosX].hasPlayer = true
        return State(newBoard, Pair(newPlayerPosX, newPlayerPosY), Pair(newBoxPosX, newBoxPosY), state.goalPos)
    }
    else if(!(newPlayerPosX == state.boxPos.first && newPlayerPosY  == state.boxPos.second) && state.board[newPlayerPosY][newPlayerPosX].type != TileType.WALL){
        newBoard[state.playerPos.second][state.playerPos.first].hasPlayer = false
        newBoard[newPlayerPosY][newPlayerPosX].hasPlayer = true
        return State(newBoard,Pair(newPlayerPosX, newPlayerPosY), state.boxPos, state.goalPos)
    }
    else{
        return state;
    }
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
    return Tmax * Math.exp(-R * t)
}

//Random number generator
fun myRand() : Int{
    return Random.nextInt()
}


//Probability function
fun p(fu:Int, fv:Int, T:Double):Double{
    return Math.exp((fu-fv) / (T*fu))
}

