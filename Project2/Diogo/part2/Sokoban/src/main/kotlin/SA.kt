import kotlin.math.exp


fun sa(Tmax: Double, Tmin: Double, R: Double, k: Int, data: Data, getInitialSolution: (data: Data) -> U,
       getRandomNeigh: (u: U, data: Data) -> U, evalFunc: (u:U, data: Data) -> Double,
       isOptimum: (fu: Double, data: Data) -> Boolean, sense: Sense): SAOut {
    var t = 0.0
    var T = Tmax
    var numEvaluations = 0
    var foundOptimum = false
    var u = getInitialSolution(data)
    var fu = evalFunc(u,data)
    println("Initial cost: $fu")
    ++numEvaluations

    while (!foundOptimum) {
        var i = 0
        while (i < k && !foundOptimum) {
            val v = getRandomNeigh(u, data)
            val fv = evalFunc(v, data)
            ++numEvaluations
            var dif = fv - fu
            if (sense == Sense.MAXIMIZE) {
                dif = -dif
            }
            if (dif < 0) {
                u = v
                fu = fv
            } else {
                val prob = p(fu, fv, T, sense)
                val x = myRand()
                if(x <= prob) {
                    u = v
                    fu = fv
                }
            }
            ++i
            if (isOptimum(fu, data)) {
                foundOptimum = true
            }
        }
        if (!foundOptimum) {
            ++t
            T = Temp(t,Tmax, R)
            if (T < Tmin) {
                break
            }
        }
    }
    println("BestCost: $fu")
    println("numEvaluations: $numEvaluations")
    return SAOut(T, numEvaluations, fu, Tmax, Tmin, R, k, u.solution, u)
}

fun Temp(t: Double, Tmax: Double, R: Double): Double =Tmax * exp(-R * t)

fun myRand(): Double = Math.random()

fun p(fu: Double, fv: Double,T: Double, sense: Sense): Double {
    return if (sense == Sense.MAXIMIZE)  exp((fv-fu) / (T*fu)) else exp((fu-fv) / (T*fu))
}

data class SAOut(var T: Double, var NumEvaluations: Int, var Cost: Double, var Tmax: Double, var Tmin: Double,
                 var R: Double, var k: Int, var u: ArrayList<Data>, var s: U)

enum class Sense{
    MAXIMIZE,MINIMIZE
}