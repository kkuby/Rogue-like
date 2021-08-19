package main

class GameBoard(var columns: Int = 14, val rows: Int = 8) {

    private val colMin = 8
    private val colMax = 16
    private val wallMin = 5
    private val wallMax = 7
    private val foodMin = 3
    private val foodMax = 5
    private val enemyMin = 7
    private val enemyMax = 9

    var player = GameObject(10, 0, 0)
    private val exit = GameObject(100, columns-1, rows-1)
    var enemyArray = ArrayList<GameObject>()
    var foodArray = ArrayList<GameObject>()

    var gridPosition = Array(columns){ Array(rows) { GameObject(-1,-1,-1) } }

    private fun initializeGrid() {
        repeat(columns){ x ->
            repeat(rows){ y ->
                gridPosition[x][y] = GameObject(0, x, y)
            }
        }
        player = GameObject(10, 0, 0)
        player.hp = 100
        gridPosition[0][0] = player
        gridPosition[columns-1][rows-1] = exit

        enemyArray = ArrayList()
        foodArray = ArrayList()
    }

    private fun setObjectAtRandom(objectCount: Int, type: Int) {
        var randomX = (0 until columns).random()
        var randomY = (0 until rows).random()
        repeat(objectCount){
            while(gridPosition[randomX][randomY].type != 0){
                randomX = (0 until columns).random()
                randomY = (0 until rows).random()
            }

            when(type){
                1 -> {
                    gridPosition[randomX][randomY].type = type
                    gridPosition[randomX][randomY].hp = 3
                }
                2 -> {
                    gridPosition[randomX][randomY].type = type
                    gridPosition[randomX][randomY].hp = 20

                    foodArray.add(gridPosition[randomX][randomY])
                }
                11 -> {
                    val ran = (0 until 2).random()
                    if(ran == 0){
                        gridPosition[randomX][randomY].type = type
                        gridPosition[randomX][randomY].damage = 10
                    }else{
                        gridPosition[randomX][randomY].type = type+1
                        gridPosition[randomX][randomY].damage = 20
                    }
                    enemyArray.add(gridPosition[randomX][randomY])
                }
            }
        }
    }

    fun setupScene(level: Int){
        when(level){
            1->{
                columns = 8
                initializeGrid()
                level1()
            }
            3->{
                columns = 8
                initializeGrid()
                level3()
            }
            5->{
                columns = 12
                initializeGrid()
                level5()
            }
            7->{
                columns = 14
                initializeGrid()
                level7()
            }
        }
    }

    fun randomStage(check: Boolean) {
        initializeGrid()
        if(check){
            //columns = (colMin .. colMax).random()
            setObjectAtRandom((wallMin .. wallMax).random(), 1)
            setObjectAtRandom((foodMin .. foodMax).random(), 2)
            setObjectAtRandom((enemyMin .. enemyMax).random(), 11)
        }

     }

    private fun level1() {
        //wall
        gridPosition[1][1].type = 1
        gridPosition[1][1].hp = 3
        gridPosition[2][1].type = 1
        gridPosition[2][1].hp = 3
        gridPosition[3][3].type = 1
        gridPosition[3][3].hp = 3
        gridPosition[3][6].type = 1
        gridPosition[3][6].hp = 3
        gridPosition[6][3].type = 1
        gridPosition[6][3].hp = 3

        //food
        gridPosition[3][1].type = 2
        gridPosition[3][1].hp = 20
        gridPosition[4][1].type = 2
        gridPosition[4][1].hp = 20
        gridPosition[4][6].type = 2
        gridPosition[4][6].hp = 20

        foodArray.add(gridPosition[3][1])
        foodArray.add(gridPosition[4][1])
        foodArray.add(gridPosition[4][6])

        //enemy
        gridPosition[3][5].type = 11
        gridPosition[3][5].damage = 10

        enemyArray.add(gridPosition[3][5])

    }

    private fun level3() {
        //wall
        gridPosition[2][2].type = 1
        gridPosition[2][2].hp = 3
        gridPosition[2][3].type = 1
        gridPosition[2][3].hp = 3
        gridPosition[1][5].type = 1
        gridPosition[1][5].hp = 3
        gridPosition[2][6].type = 1
        gridPosition[2][6].hp = 3
        gridPosition[6][6].type = 1
        gridPosition[6][6].hp = 3
        gridPosition[4][5].type = 1
        gridPosition[4][5].hp = 3

        //food
        gridPosition[1][1].type = 2
        gridPosition[1][1].hp = 20
        gridPosition[6][4].type = 2
        gridPosition[6][4].hp = 20
        gridPosition[5][6].type = 2
        gridPosition[5][6].hp = 20

        foodArray.add(gridPosition[1][1])
        foodArray.add(gridPosition[6][4])
        foodArray.add(gridPosition[5][6])

        //enemy
        gridPosition[3][3].type = 11
        gridPosition[3][3].damage = 10
        gridPosition[4][4].type = 12
        gridPosition[4][4].damage = 20
        gridPosition[6][3].type = 11
        gridPosition[6][3].damage = 10

        enemyArray.add(gridPosition[3][3])
        enemyArray.add(gridPosition[4][4])
        enemyArray.add(gridPosition[6][3])

    }

    private fun level5() {
        //wall
        gridPosition[2][6].type = 1
        gridPosition[2][6].hp = 3
        gridPosition[4][2].type = 1
        gridPosition[4][2].hp = 3
        gridPosition[4][4].type = 1
        gridPosition[4][4].hp = 3
        gridPosition[6][3].type = 1
        gridPosition[6][3].hp = 3
        gridPosition[8][4].type = 1
        gridPosition[8][4].hp = 3
        gridPosition[10][2].type = 1
        gridPosition[10][2].hp = 3
        gridPosition[10][4].type = 1
        gridPosition[10][4].hp = 3
        gridPosition[10][6].type = 1
        gridPosition[10][6].hp = 3
        gridPosition[9][6].type = 1
        gridPosition[9][6].hp = 3


        //food
        gridPosition[1][1].type = 2
        gridPosition[1][1].hp = 20
        gridPosition[2][1].type = 2
        gridPosition[2][1].hp = 20
        gridPosition[4][5].type = 2
        gridPosition[4][5].hp = 20
        gridPosition[6][2].type = 2
        gridPosition[6][2].hp = 20
        gridPosition[7][3].type = 2
        gridPosition[7][3].hp = 20

        foodArray.add(gridPosition[1][1])
        foodArray.add(gridPosition[2][1])
        foodArray.add(gridPosition[4][5])
        foodArray.add(gridPosition[6][2])
        foodArray.add(gridPosition[7][3])

        //enemy
        gridPosition[3][2].type = 11
        gridPosition[3][2].damage = 10
        gridPosition[3][3].type = 12
        gridPosition[3][3].damage = 20
        gridPosition[6][4].type = 11
        gridPosition[6][4].damage = 10
        gridPosition[9][2].type = 12
        gridPosition[9][2].damage = 20
        gridPosition[9][4].type = 11
        gridPosition[9][4].damage = 10

        enemyArray.add(gridPosition[3][2])
        enemyArray.add(gridPosition[3][3])
        enemyArray.add(gridPosition[6][4])
        enemyArray.add(gridPosition[9][2])
        enemyArray.add(gridPosition[9][4])

    }

    private fun level7() {
        //wall
        gridPosition[0][2].type = 1
        gridPosition[0][2].hp = 3
        gridPosition[0][6].type = 1
        gridPosition[0][6].hp = 3
        gridPosition[3][0].type = 1
        gridPosition[3][0].hp = 3
        gridPosition[3][4].type = 1
        gridPosition[3][4].hp = 3
        gridPosition[4][4].type = 1
        gridPosition[4][4].hp = 3
        gridPosition[5][0].type = 1
        gridPosition[5][0].hp = 3
        gridPosition[5][5].type = 1
        gridPosition[5][5].hp = 3
        gridPosition[6][0].type = 1
        gridPosition[6][0].hp = 3
        gridPosition[6][3].type = 1
        gridPosition[6][3].hp = 3
        gridPosition[8][2].type = 1
        gridPosition[8][2].hp = 3
        gridPosition[8][3].type = 1
        gridPosition[8][3].hp = 3
        gridPosition[8][4].type = 1
        gridPosition[8][4].hp = 3
        gridPosition[9][5].type = 1
        gridPosition[9][5].hp = 3
        gridPosition[9][6].type = 1
        gridPosition[9][6].hp = 3
        gridPosition[9][7].type = 1
        gridPosition[9][7].hp = 3
        gridPosition[10][1].type = 1
        gridPosition[10][1].hp = 3
        gridPosition[10][2].type = 1
        gridPosition[10][2].hp = 3
        gridPosition[10][3].type = 1
        gridPosition[10][3].hp = 3
        gridPosition[10][7].type = 1
        gridPosition[10][7].hp = 3
        gridPosition[11][4].type = 1
        gridPosition[11][4].hp = 3
        gridPosition[12][3].type = 1
        gridPosition[12][3].hp = 3
        gridPosition[12][6].type = 1
        gridPosition[12][6].hp = 3


        //food
        gridPosition[0][4].type = 2
        gridPosition[0][4].hp = 20
        gridPosition[0][5].type = 2
        gridPosition[0][5].hp = 20
        gridPosition[2][4].type = 2
        gridPosition[2][4].hp = 20
        gridPosition[2][7].type = 2
        gridPosition[2][7].hp = 20
        gridPosition[9][0].type = 2
        gridPosition[9][0].hp = 20

        foodArray.add(gridPosition[0][4])
        foodArray.add(gridPosition[0][5])
        foodArray.add(gridPosition[2][4])
        foodArray.add(gridPosition[2][7])
        foodArray.add(gridPosition[9][0])

        //enemy
        gridPosition[2][2].type = 11
        gridPosition[2][2].damage = 10
        gridPosition[4][7].type = 12
        gridPosition[4][7].damage = 20
        gridPosition[5][2].type = 11
        gridPosition[5][2].damage = 10
        gridPosition[7][6].type = 12
        gridPosition[7][6].damage = 20
        gridPosition[8][5].type = 11
        gridPosition[8][5].damage = 10
        gridPosition[10][4].type = 12
        gridPosition[10][4].damage = 20
        gridPosition[11][0].type = 11
        gridPosition[11][0].damage = 10
        gridPosition[11][7].type = 12
        gridPosition[11][7].damage = 20
        gridPosition[12][4].type = 11
        gridPosition[12][4].damage = 10

        enemyArray.add(gridPosition[2][2])
        enemyArray.add(gridPosition[4][7])
        enemyArray.add(gridPosition[5][2])
        enemyArray.add(gridPosition[8][5])
        enemyArray.add(gridPosition[7][6])
        enemyArray.add(gridPosition[10][4])
        enemyArray.add(gridPosition[11][0])
        enemyArray.add(gridPosition[11][7])
        enemyArray.add(gridPosition[12][4])

    }

}