package ktshsResearchAssignmentE2022.com.github.minesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

private fun List<List<TileState>>.incAround(x: Int, y: Int) {
    // 左上
    if (x != 0 && y != 0) {
        if (!this[x - 1][y - 1].isMine) this[x - 1][y - 1].numOfAroundMines++
    }
    // 真上
    if (x != 0) {
        if (!this[x - 1][y].isMine) this[x - 1][y].numOfAroundMines++
    }
    // 右上
    if (x != 0 && this[x - 1].size > y + 1) {
        if (!this[x - 1][y + 1].isMine) this[x - 1][y + 1].numOfAroundMines++
    }

    // 左
    if (y != 0) {
        if (!this[x][y - 1].isMine) this[x][y - 1].numOfAroundMines++
    }
    // 右
    if (this[x].size > y + 1) {
        if (!this[x][y + 1].isMine) this[x][y + 1].numOfAroundMines++
    }

    // 左下
    if (this.size > x + 1 && y != 0) {
        if (!this[x + 1][y - 1].isMine) this[x + 1][y - 1].numOfAroundMines++
    }
    // 真下
    if (this.size > x + 1) {
        if (!this[x + 1][y].isMine) this[x + 1][y].numOfAroundMines++
    }
    // 右下
    if (this.size > x + 1 && this[x + 1].size > y + 1) {
        if (!this[x + 1][y + 1].isMine) this[x + 1][y + 1].numOfAroundMines++
    }
}

class MineSweeperLogic(val column: Int, val row: Int, numOfMines: Int, seed: Int) {
    val map: List<List<TileState>>
    private val flaggedTilesCoordinate = mutableSetOf<Pair<Int, Int>>()
    private val minesCoordinate: Set<Pair<Int, Int>>
    var isGameOver by mutableStateOf(false)
    var isGameClear by mutableStateOf(false)
    var isDevMode by mutableStateOf(false)

    init {
        val connectedList = mutableListOf<TileState>()
        val mutMinesMap = mutableSetOf<Pair<Int, Int>>()

        for (i in 1..numOfMines) {
            connectedList.add(TileState(true))
        }

        while (connectedList.size <= column * row) {
            connectedList.add(TileState(false))
        }

        connectedList.shuffle(Random(seed))
        map = connectedList.windowed(column, column)

        for (x in 0 until row) {
            for (y in 0 until column) {
                if (map[x][y].isMine) {
                    mutMinesMap.add(Pair(x, y))
                    map.incAround(x, y)
                }
            }
        }

        minesCoordinate = mutMinesMap
    }

    fun openTileWithAround(x: Int, y: Int) {
        openTile(x, y)
        if (map[x][y].numOfAroundMines == 0) {
            map.openAround(x, y)
        }
    }

    fun toggleTileFlag(x: Int, y: Int) {
        map[x][y].isFlagged = !map[x][y].isFlagged
        if (map[x][y].isFlagged) flaggedTilesCoordinate.add(Pair(x, y)) else flaggedTilesCoordinate.remove(Pair(x, y))
        isGameClear = minesCoordinate == flaggedTilesCoordinate
    }

    private fun openTile(x: Int, y: Int) {
        map[x][y].isOpened = true
        if (map[x][y].isMine && !isDevMode) isGameOver = true
    }


    private fun List<List<TileState>>.openAround(x: Int, y: Int) {
        // 左上
        if (x != 0 && y != 0 && !this[x - 1][y - 1].isOpened) {
            openTile(x - 1, y - 1)
        }
        // 真上
        if (x != 0 && !this[x - 1][y].isOpened) {
            openTile(x - 1, y)
            if (this[x - 1][y].numOfAroundMines == 0) {
                this.openAround(x - 1, y)
            }
        }
        // 右上
        if (x != 0 && this[x - 1].size > y + 1 && !this[x - 1][y + 1].isOpened) {
            openTile(x - 1, y + 1)
        }
        // 左
        if (y != 0 && !this[x][y - 1].isOpened) {
            openTile(x, y - 1)
            if (this[x][y - 1].numOfAroundMines == 0) {
                this.openAround(x, y - 1)
            }
        }
        // 右
        if (this[x].size > y + 1 && !this[x][y + 1].isOpened) {
            openTile(x, y + 1)
            if (this[x][y + 1].numOfAroundMines == 0) {
                this.openAround(x, y + 1)
            }
        }
        // 左下
        if (this.size > x + 1 && y != 0 && !this[x + 1][y - 1].isOpened) {
            openTile(x + 1, y - 1)
        }
        // 真下
        if (this.size > x + 1 && !this[x + 1][y].isOpened) {
            openTile(x + 1, y)
            if (this[x + 1][y].numOfAroundMines == 0) {
                this.openAround(x + 1, y)
            }
        }
        // 右下
        if (this.size > x + 1 && this[x + 1].size > y + 1 && !this[x + 1][y + 1].isOpened) {
            openTile(x + 1, y + 1)
        }
    }
}
