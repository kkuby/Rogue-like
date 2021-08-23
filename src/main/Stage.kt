package main

class Stage(val columns: Int, val wallCount: Int, val food1Count: Int, val food2Count: Int, val enemy1Count: Int, val enemy2Count: Int) {
    val rows: Int = 8
    val gridPosition = Array(columns){ Array(rows) { GameObject() } }

    fun addGridPosition(map: Array<Array<GameObject>>){
        gridPosition.forEachIndexed { i, arrayOfGameObjects ->
            arrayOfGameObjects.forEachIndexed { j, _ ->
                gridPosition[i][j] = GameObject(map[i][j])
            }
        }
    }
}