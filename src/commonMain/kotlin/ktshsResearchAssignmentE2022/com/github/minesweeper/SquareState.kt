package ktshsResearchAssignmentE2022.com.github.minesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface ISquareState {
    val x: Int
    val y: Int
    var isOpened: Boolean
    var isFlagged: Boolean
}

data class NormalSquareState(
    override val x: Int,
    override val y: Int,
    val numOfAroundMines: Int
) : ISquareState {
    override var isOpened by mutableStateOf(false)
    override var isFlagged by mutableStateOf(false)
}

data class MineSquareState(
    override val x: Int,
    override val y: Int
) : ISquareState {
    override var isOpened by mutableStateOf(false)
    override var isFlagged by mutableStateOf(false)
}
