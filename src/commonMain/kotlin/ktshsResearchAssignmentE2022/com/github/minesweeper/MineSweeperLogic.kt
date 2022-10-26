package ktshsResearchAssignmentE2022.com.github.minesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.js.Date
import kotlin.random.Random

class MineSweeperLogic(val xLength: Int, val yLength: Int, val numOfMines: Int, val seed: Int) {
    // y軸方向に各マスの情報を格納している
    val board: List<List<ISquareState>>
    var gameState by mutableStateOf(GameState.BeforeStarts)
        private set
    private val coordinatesOfOpened = mutableSetOf<Pair<Int, Int>>()
    private val coordinatesWithoutMines: Set<Pair<Int, Int>>

    var isDevMode by mutableStateOf(false)
    private var startTime = 0.0

    init {
        val coordinatesOfMines = mutableSetOf<Pair<Int, Int>>()
        val rnd = Random(seed)
        while (coordinatesOfMines.size < numOfMines) {
            coordinatesOfMines.add(Pair(rnd.nextInt(xLength), rnd.nextInt(yLength)))
        }

        val coordinatesOfPlane = mutableSetOf<Pair<Int, Int>>()
        for (x in 0 until xLength) {
            for (y in 0 until yLength) {
                coordinatesOfPlane.add(Pair(x, y))
            }
        }

        board = MutableList(xLength) { x ->
            MutableList(yLength) { y ->
                if (coordinatesOfMines.contains(Pair(x, y))) {
                    MineSquareState(x, y)
                } else {
                    NormalSquareState(x, y, 0)
                }
            }
        }

        coordinatesOfPlane.removeAll(coordinatesOfMines)
        coordinatesWithoutMines = coordinatesOfPlane
    }

    fun openTileWithAround(x: Int, y: Int) {
        if (gameState == GameState.BeforeStarts) firstTimeAction()
        if (board[x][y].isFlagged) return
        openTile(x, y)
        if (board[x][y] is NormalSquareState && (board[x][y] as NormalSquareState).numOfAroundMines == 0) {
            board.openAround(x, y)
        }
    }

    fun toggleTileFlag(x: Int, y: Int) {
        if (gameState == GameState.BeforeStarts) firstTimeAction()
        board[x][y].isFlagged = !board[x][y].isFlagged
    }

    fun getElapsedSeconds(): Double {
        return (Date.now() - startTime) / 1000
    }

    private fun firstTimeAction() {
        gameState = GameState.Started
        startTime = Date.now()
    }

    private fun openTile(x: Int, y: Int) {
        if (board[x][y].isFlagged) toggleTileFlag(x, y)
        board[x][y].isOpened = true
        coordinatesOfOpened.add(Pair(x, y))
        if (coordinatesOfOpened == coordinatesWithoutMines) {
            gameState = GameState.GameClear
        } else if (board[x][y] is MineSquareState && !isDevMode) {
            gameState = GameState.GameOver
        }
    }


    private fun List<List<ISquareState>>.openAround(x: Int, y: Int) {
        for (cx in x - 1..x + 1) {
            if (0 <= cx && cx < this.size) {
                for (cy in y - 1..y + 1) {
                    if (0 <= cy && cy < this[cx].size && !this[cx][cy].isOpened) {
                        openTile(cx, cy)
                        if (board[cx][cy] is NormalSquareState && (this[cx][cy] as NormalSquareState).numOfAroundMines == 0)
                            this.openAround(cx, cy)
                    }
                }
            }
        }
    }
}
