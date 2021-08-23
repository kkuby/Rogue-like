package main

class GameBoard(var columns: Int = 8) {

    val rows: Int = 8

    var wallRate = 10
    var food1Rate = wallRate+4
    var food2Rate = food1Rate+3
    var enemy1Rate = food2Rate+5
    var enemy2Rate = enemy1Rate+4

    var player = GameObject(10, 0, 0)
    private val exit = GameObject(100, columns-1, rows-1)
    var enemyArray = ArrayList<GameObject>()
    var foodArray = ArrayList<GameObject>()

    var gridPosition = Array(columns){ Array(rows) { GameObject() } }

    fun randomGrid() : Stage {
        player.hp = 100
        gridPosition[0][0] = player
        gridPosition[columns-1][rows-1] = exit

        var wallCount = 0
        var food1Count = 0
        var food2Count = 0
        var enemy1Count = 0
        var enemy2Count = 0

        repeat(columns){ x ->
            repeat(rows){ y ->
                if(gridPosition[x][y] != player && gridPosition[x][y] != exit){
                    gridPosition[x][y].x = x
                    gridPosition[x][y].y = y
                    val ran = (0 until 100).random()
                    when {
                        ran < wallRate -> {
                            gridPosition[x][y].type = 1
                            gridPosition[x][y].hp = 3
                            wallCount++
                        }
                        ran in wallRate until food1Rate -> {
                            gridPosition[x][y].type = 2
                            gridPosition[x][y].hp = 10
                            foodArray.add(gridPosition[x][y])
                            food1Count++
                        }
                        ran in food1Rate until food2Rate -> {
                            gridPosition[x][y].type = 3
                            gridPosition[x][y].hp = 20
                            foodArray.add(gridPosition[x][y])
                            food2Count++
                        }
                        ran in food2Rate until enemy1Rate -> {
                            gridPosition[x][y].type = 11
                            gridPosition[x][y].damage = 10
                            enemyArray.add(gridPosition[x][y])
                            enemy1Count++
                        }
                        ran in enemy1Rate until enemy2Rate -> {
                            gridPosition[x][y].type = 12
                            gridPosition[x][y].damage = 20
                            enemyArray.add(gridPosition[x][y])
                            enemy2Count++
                        }
                        else -> {
                            gridPosition[x][y].type = 0
                        }
                    }

                }
            }
        }

        val stage = Stage(columns, wallCount, food1Count, food2Count, enemy1Count, enemy2Count)
        stage.addGridPosition(gridPosition)
        return stage
    }

}