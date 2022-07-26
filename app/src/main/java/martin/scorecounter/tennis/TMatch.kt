package martin.scorecounter.tennis

class TMatch constructor(matchType: String, p1: String, p2: String, p12: String?, p22: String?, p1FirstServe: Boolean, setsToWin: Int, matchTieBreak: Boolean){

    val matchType: String = matchType
    val player1: String = p1
    val player2: String = p2
    val player12: String? = p12
    val player22: String? = p22
    val matchTieBreak: Boolean = matchTieBreak

    var matchWinner: Int = 0
    var decidingSet: Boolean = false

    val setsToWin: Int = setsToWin

    var p1Sets = 0
    var p2Sets = 0

    var currentSetNr = 0
    var p1Serving: Boolean = p1FirstServe

    var setList: MutableList<TSet> = mutableListOf()

    var finished: Boolean = false

    fun setWon(byPlayer: Int, p1LastServe: Boolean){
        if (byPlayer == 1) {
            p1Sets++
        } else if (byPlayer == 2) {
            p2Sets++
        }

        if (p1Sets == setsToWin){
            matchWinner = 1
            finished = true
        } else if (p2Sets == setsToWin){
            matchWinner = 2
            finished = true
        } else {
            if (p1Sets == setsToWin-1 && p2Sets == setsToWin-1){
                decidingSet = true
            }
            p1Serving = !p1LastServe
            newSet()
        }
    }

    fun newSet(): TSet {
        currentSetNr++
        var mtb = false
        if (decidingSet){
            mtb = matchTieBreak
        }
        var newSet = TSet(currentSetNr, p1Serving, mtb)
        setList.add(newSet)

        return setList[currentSetNr-1]
    }

    fun getCurrentSet(): TSet {
        return setList[currentSetNr-1]
    }

    fun getSetByNr(setNr: Int): TSet {
        return setList[setNr-1]
    }

}