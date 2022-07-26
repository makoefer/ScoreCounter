package martin.scorecounter.tennis

import android.content.Context

class TGame constructor(gameNr: Int, p1Serving: Boolean, tiebreak: Boolean, matchTiebreak: Boolean) {

    val id = gameNr
    var p1Serving = p1Serving
    val tiebreak = tiebreak
    val matchTiebreak = matchTiebreak
    var tiebreakPoints = 0

    var p1Points = 0
    var p2Points = 0
    var gameWinner = 0
    var lastPoint = TPoint("0", "0")
    var pointHistory: MutableList<TPoint> = mutableListOf(lastPoint)
    var finished = false

    var p1BreakPoint = false
    var p2BreakPoint = false

    private val points = arrayOf("0", "15", "30", "40", "AD", "W")

    fun pointWon(byPlayer: Int){
        if (byPlayer == 1){
            p1Points++
        } else if (byPlayer == 2) {
            p2Points++
        }

        if (!tiebreak){
            // checking game win
            if (points[p1Points] == "AD" && points[p2Points] == "AD"){
                p1Points--; p2Points--
            } else if ((points[p1Points] == "AD" && points[p2Points] != "40") || points[p1Points] == "W"){
                gameWinner = 1

                finished = true
            } else if ((points[p2Points] == "AD" && points[p1Points] != "40") || points[p2Points] == "W"){
                gameWinner = 2

                finished = true
            }
            lastPoint = TPoint(points[p1Points], points[p2Points])
        } else {
            if (matchTiebreak) tiebreakPoints = 10 else tiebreakPoints = 7
            if (p1Points >= tiebreakPoints && p1Points-p2Points >= 2){
                gameWinner = 1
                finished = true
            } else if (p2Points >= tiebreakPoints && p2Points-p1Points >= 2){
                gameWinner = 2
                finished = true
            }
            lastPoint = TPoint(p1Points.toString(), p2Points.toString())
            if ((p1Points+p2Points)%2 != 0){
                p1Serving = !p1Serving
            }
        }
        if (!finished && !tiebreak){
            p1BreakPoint = !p1Serving && ((points[p1Points+1] == "AD" && p2Points<p1Points) || points[p1Points+1] == "W")
            p2BreakPoint = p1Serving && ((points[p2Points+1] == "AD" && p1Points<p2Points) || points[p2Points+1] == "W")
        }

        pointHistory.add(lastPoint)
        //pointHistory.forEach {Log.d("TENNIS", it.toString())}
        //Log.d("TENNIS", finished.toString())

    }
}