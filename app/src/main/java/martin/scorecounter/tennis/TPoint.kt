package martin.scorecounter.tennis

class TPoint constructor(p1: String, p2: String) {
    val p1 = p1
    val p2 = p2
    override fun toString(): String {
        return "{$p1, $p2}"
    }

}