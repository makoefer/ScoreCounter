package martin.scorecounter.tennis

class TSet constructor(setNr: Int, p1FirstServe: Boolean, matchTieBreak: Boolean) {

    val setNr = setNr
    var p1Games = 0
    var p2Games = 0
    var setWinner = 0
    var finished = false

    var tieBreak = false
    var matchTieBreak = matchTieBreak

    var currentGameNr = 0
    var p1Serving: Boolean = p1FirstServe

    var gameList: MutableList<TGame> = mutableListOf()

    fun gameWon(byPlayer: Int){
        if (byPlayer == 1){
            p1Games++
        } else if (byPlayer == 2){
            p2Games++
        }

        if (p1Games >= 6 && p1Games-p2Games >= 2 || tieBreak && p1Games == 7 || matchTieBreak && p1Games == 1) {
            setWinner = 1
            finished = true
        } else if (p2Games >= 6 && p2Games-p1Games >= 2 || tieBreak && p2Games == 7 || matchTieBreak && p2Games == 1){
            setWinner = 2
            finished = true
        } else {
            if (p1Games == 6 && p2Games == 6){
                tieBreak = true
            }
            p1Serving = !p1Serving
            newGame()
        }
    }

    fun newGame(): TGame {
        if (matchTieBreak){
            tieBreak = true
        }
        currentGameNr++
        var newGame = TGame(currentGameNr, p1Serving, tieBreak, matchTieBreak)
        gameList.add(newGame)

        return gameList[currentGameNr-1]
    }

    fun getCurrentGame(): TGame {
        return gameList[currentGameNr-1]
    }

    fun getGameByNr(gameNr: Int): TGame {
        return gameList[gameNr-1]
    }

}