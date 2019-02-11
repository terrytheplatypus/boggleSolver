import com.beust.klaxon.Klaxon
import java.io.File


class BoggleBoard(val dictionary: List<String>, val board: List<List<String>>)

fun main(args: Array<String>) {

    /*
    val boardAndDict = Klaxon().parse<BoggleBoard>(File("src/main/kotlin/testBoggle.json"))
    if(boardAndDict!= null) {
        println(boardAndDict.board + "\n " + boardAndDict.dictionary)

    }
    */
    val s:BoggleSolver = BoggleSolver()
    val fileName = "src/main/kotlin/testBoggle.json"
    val solution = s.solve("src/main/kotlin/testBoggle.json")
    //print(solution)

}
/*
make recursive function to check for each word, and add helper func just to make it neat
recursive function needs to keep track of the remaining letters in the word, current square,
 and previously visited squares in the current path
square index can be represented as a string split by comma, or a length 2 array
(will use 2-index array)
 */
/*
not necessary to make a separate func for this, but make a func that gets vals in adjacent squares
as well as each respective index (should make new object for this,
takes a len 2 array and a board

 */
class indexAndLetter(val index: IntArray, val letter: Char)

fun  getAdjacent( index: IntArray ,board:List<List<String>>): List<indexAndLetter> {
    //
    val adjSquares:MutableList<indexAndLetter> = mutableListOf<indexAndLetter>()
    val ind1 = index[0]
    val ind2 = index[1]
    for (i in ind1-1..ind1+1) {

        for(j in ind2 - 1 .. ind2+1){
            //checking that it's not the center piece, and that
            //it's not outside the boundaries of the grid
            if(!(ind1 == i && ind2 == j)
                && i>-1 && i<3
                && j>-1 && j<3) {
                val currInd: IntArray = intArrayOf(i, j)
                adjSquares.add(indexAndLetter(currInd, board[i][j].single()))
            }
        }
    }
    return adjSquares
}



class BoggleSolver() {
    /**
     * @return list of the words in the solution
     *
     */
    public fun solve(input:String):List<String> {
        val solution: MutableList <String> = mutableListOf()
        val boardAndDict = Klaxon().parse<BoggleBoard>(File(input))
        if(boardAndDict!= null) {

            val dict: List<String> = boardAndDict.dictionary
            val board: List<List<String>> = boardAndDict.board

            //next variable is for nicely formatted printing
            var firstWordFound = false
            print("Solution to boggle board:\n")
            for( word in dict) {
                //start with looking through all the squares to find the first letter
                //whenever you find the first letter, check if recursiveSolve returns true

                for(i in 0..2) {
                    for (j in 0..2) {
                        //look for first letter, if you find it call the recursive
                        var charList:List<String> = board[i]
                        val currCell = charList[j].single()
                        val firstLetter:Char = word[0]
                        if(currCell == firstLetter) {
                            //if(recursiveSolve() == true) {
                            //last argument to function here is the first index of the letter
                            if(recursiveSolve(
                                    word.substring(1),
                                    board,
                                    intArrayOf(i,j),
                                    listOf(intArrayOf(i,j)) )) {
                                solution.add(word)
                                if(firstWordFound) print(", ")
                                else firstWordFound = true
                                print(word)
                            }
                        }
                    }

                }
            }

        }
        //for all words in dictionary, do recursiveSolve, and if recursiveSolve is false,
        //don't add the word, but if it returns true, add the word
        return solution
    }
    /*
    recursive function needs to keep track of the remaining letters in the word, the board
    current square, and previously visited squares in the current path
     */
    private fun recursiveSolve(remnant:String, board:List<List<String>>,
                       curr:IntArray, prevVisited: List<IntArray> ) : Boolean {



        var newList:MutableList<IntArray> = prevVisited.toMutableList()
        /*
        when recursively iterating through the adjacent edges
        if one of the paths returns false, keep going, but if one of
        the paths returns true, then stop iterating
        */
        if(remnant.isEmpty()) return true
        //get adjacent cells



        val adjCells: List<indexAndLetter> = getAdjacent(curr,board)



        for(cell in adjCells) {




            //

            //if a cell matches the first remaining letter, do recursive and
            //add the cell to prev visited list
            if(cell.letter==remnant[0]) {
                //first check if current cell is previously visited
                if(isCellInList(cell.index,prevVisited)) continue
                newList.add(cell.index)
                //if recursive call returns true, continue returning true
                //recursive call cuts the first character off the remaining
                //characters
                if (recursiveSolve(
                        remnant.substring(1),
                        board,
                        cell.index,
                        newList
                        )) {
                    return true
                }
            }
        }
        return false
    }

    //i was considering doing this passing the previously visited cells as a set
    //instead of a list, which might save a lot of time for very big board sizes,
    //but because this problem is 3x3. it doesn't make too much difference time-wise

    fun isCellInList( cell:IntArray, listOfCells:List<IntArray>):Boolean {
        for(curCell in listOfCells)
            if (cell.contentEquals(curCell)) return true
        return false
    }

}