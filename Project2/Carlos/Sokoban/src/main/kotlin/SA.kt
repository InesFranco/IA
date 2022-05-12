import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.exp


fun sa(Tmax: Double, Tmin: Double, R: Double, k: Int, data: Int, getInitialSolution: (data: Int) -> Double,
       getRandomNeigh: (u: Double, data: Int) -> Double, evalFunc: (u:Double, data: Int) -> Double,
       isOptimum: (fu: Double, data: Int) -> Boolean, sense: String): Struct {
    var t = 0.0
    var T = Tmax
    var numEvaluations = 0
    var foundOptimum = false
    var u = getInitialSolution(data)
    var fu = evalFunc(u,data)
    println("Initial cost: $fu")
    ++numEvaluations

    var z = 1
    var F = ArrayList<Double>()
    F[z] = fu
    //F(z) = fu
    ++z
    while (!foundOptimum) {
        var i = 0
        while (i < k && !foundOptimum) {
            var v = getRandomNeigh(u, data)
            var fv = evalFunc(v, data)
            ++numEvaluations
            var dif = fv - fu
            if (sense.equals("maximize")) {
                dif = -dif
            }
            if (dif < 0) {
                u = v
                fu = fv
            } else {
                var prob = p(fu, fv, T, sense)
                var x = myRand()
                if(x <= prob) {
                    u = v
                    fu = fv
                }
            }
            ++i
            F[z] = fu//F(z) = fu
            ++z
        }

        if (isOptimum(fu, data)) {
            foundOptimum = true
        }

        if (!foundOptimum) {
            ++t
            T = Temp(t,Tmax, R)
            if (T < Tmin) {
                break
            }
        }
    }
        println("BestCost: " + fu)
        println("numEvaluations: "+ numEvaluations)
        return Struct(T, numEvaluations, fu, Tmax, Tmin, R, k, u.solution, F, u)
}

fun Temp(t: Double, Tmax: Double, R: Double): Double =Tmax * exp(-R * t)

fun myRand(): Double = Math.random()

fun p(fu: Double, fv: Double,T: Double, sense: String): Double {
    return if (sense.equals("maximize"))  exp((fv-fu) / (T*fu)) else exp((fu-fv) / (T*fu))
}

public class Struct(T: Double, NumEvaluations: Int, Cost: Double, Tmax: Double, Tmin: Double, R: Double, k: Int, u: Int, F: java.util.ArrayList<Double>, s: Double)