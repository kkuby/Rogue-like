import kotlin.math.log

class GameBoard {

    private val columns = 8
    private val rows = 8
    private val wallCountMin = 5
    private val wallCountMax = 9
    private val foodCountMin = 3
    private val foodCountMax = 5
    val player = GameObject(10, 0, 0)
    val exit = GameObject(100, columns-1, rows-1)
    val enemyArray = ArrayList<GameObject>()

    var gridPosition = Array(columns){ Array(rows) {GameObject(-1,-1,-1)} }

    private fun initializeGrid() {
        repeat(columns){ x ->
            repeat(rows){ y ->
                gridPosition[x][y] = GameObject(0, x, y)
            }
        }
        player.hp = 100
        gridPosition[0][0] = player
        gridPosition[columns-1][rows-1] = exit
    }

    private fun setObjectAtRandom(objectCount: Int, type: Int) {
        var randomX = (1 until columns-1).random()
        var randomY = (1 until rows-1).random()
        repeat(objectCount){
            while(gridPosition[randomX][randomY].type != 0){
                randomX = (1 until columns-1).random()
                randomY = (1 until rows-1).random()
            }

            when(type){
                1 -> gridPosition[randomX][randomY].hp = 4
                2 -> gridPosition[randomX][randomY].hp = 10
                3 -> gridPosition[randomX][randomY].hp = 20
                11 -> gridPosition[randomX][randomY].damage = 10
                12 -> gridPosition[randomX][randomY].damage = 20
            }

            gridPosition[randomX][randomY].type = type
        }
    }

    private fun fixedObject() {
        gridPosition[1][1].type = 1
        gridPosition[1][1].hp = 4
        gridPosition[2][1].type = 1
        gridPosition[2][1].hp = 4
        gridPosition[3][1].type = 2
        gridPosition[3][1].hp = 10
        gridPosition[4][1].type = 2
        gridPosition[4][1].hp = 10

        gridPosition[3][3].type = 1
        gridPosition[3][3].hp = 4
        gridPosition[6][3].type = 1
        gridPosition[6][3].hp = 4

        gridPosition[3][5].type = 11
        gridPosition[3][5].damage = 10
//        gridPosition[5][5].type = 12
//        gridPosition[5][5].damage = 20

        enemyArray.add(gridPosition[3][5])
        //enemyArray.add(gridPosition[5][5])

        gridPosition[3][6].type = 1
        gridPosition[3][6].hp = 4
        gridPosition[4][6].type = 2
        gridPosition[4][6].hp = 10
    }

    fun setupScene(level: Int){
        initializeGrid()
//        setObjectAtRandom((wallCountMin .. wallCountMax).random(), 1)
//        setObjectAtRandom((foodCountMin .. foodCountMax).random(), 2)
//        val enemyCount = log(level.toFloat(), 2f).toInt()
//        setObjectAtRandom(enemyCount, 11)
        fixedObject()

    }

}