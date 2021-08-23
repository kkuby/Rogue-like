package main

import kotlin.math.pow

class GameManager{

    private var gameBoard = GameBoard()
    var gridPosition = gameBoard.gridPosition

    var player = gameBoard.player
    var enemyArray = gameBoard.enemyArray
    var foodArray = gameBoard.foodArray

    val columns = gameBoard.columns
    val rows = gameBoard.rows

    val savePosition = Array(columns){ Array(rows) { GameObject(-1,-1,-1) } }
    val saveEnemies = ArrayList<GameObject>()
    val saveFoods = ArrayList<GameObject>()

    companion object {

        private var playerTurn = true
        private var enemyTurn = 0

        private var turn = 0
        private var routeArray = ArrayList<Pair<Int, Int>>()
        private var historyArray = ArrayList<Pair<Int, Int>>()
        private var isClear = false

        @Volatile private var instance: GameManager? = null
        @JvmStatic fun getInstance(): GameManager =
            instance ?: synchronized(this) {
                instance ?: GameManager().also {
                    instance = it
                }
            }
    }

    /**
     * play
     */
    fun gameInit(level: Int) {
        gameBoard = GameBoard()
        if(level>0){
            gameBoard.setupScene(level)
            gridPosition = gameBoard.gridPosition
            enemyArray = gameBoard.enemyArray
            foodArray = gameBoard.foodArray

            player = gameBoard.player
        }else{
            if(savePosition[1][1].type == -1){
                gameBoard.randomStage(true)
                gridPosition = gameBoard.gridPosition
                enemyArray = gameBoard.enemyArray
                foodArray = gameBoard.foodArray

                saveEnemies.clear()
                enemyArray.forEach {
                    saveEnemies.add(GameObject(it))
                }
                saveFoods.clear()
                foodArray.forEach {
                    saveFoods.add(GameObject(it))
                }

                gridPosition.forEachIndexed { i, arrayOfGameObjects ->
                    arrayOfGameObjects.forEachIndexed { j, gameObject ->
                        savePosition[i][j] = GameObject(gameObject)
                        print("${gameObject.type} ")
                    }
                }
                println()

                player = gameBoard.player

            }else{
                //gameBoard.randomStage(false)
                savePosition.forEachIndexed { i, arrayOfGameObjects ->
                    arrayOfGameObjects.forEachIndexed { j, gameObject ->
                        gridPosition[i][j] = GameObject(gameObject)
                    }
                }

                enemyArray.clear()
                saveEnemies.forEach {
                    enemyArray.add(GameObject(it))
                }
                foodArray.clear()
                saveFoods.forEach {
                    foodArray.add(GameObject(it))
                }

                player = GameObject(10, 0, 0)
                player.hp = 100

                gridPosition[0][0] = GameObject(player)

            }
        }
        playerTurn = true
        enemyTurn = 0
        turn = 0
        routeArray = ArrayList()
        historyArray = ArrayList()
        isClear = false
    }

    fun setGrid(grid: Array<Array<GameObject>>, p: GameObject, enemies: ArrayList<GameObject>, foods: ArrayList<GameObject>) {
        gridPosition = grid
        player = p
        enemyArray = enemies
        foodArray = foods
        gameBoard.gridPosition = grid
        gameBoard.enemyArray = enemies
        gameBoard.foodArray = foods

    }

    fun randomSearch(): Solution {
        while(playerTurn){
            move(player, player.randomPlayerMovementDirection())

            enemyArray.forEach { enemy ->
                when(enemy.type){
                    11->{
                        if (enemyTurn % 2 == 0) {
                            move(enemy, enemy.enemyMovementDirection(player))
                        }
                    }
                    12-> {
                        move(enemy, enemy.enemyMovementDirection(player))
                    }
                }
            }
            enemyTurn++
        }

        return Solution(isClear, turn, player, routeArray)

    }

    fun routeValidation(route: List<Pair<Int, Int>>): Solution {
        route.forEach {
            if(playerTurn) {
                move(player, it)
                enemyArray.forEach { enemy ->
                    when(enemy.type){
                        11->{
                            if (enemyTurn % 2 == 0) {
                                move(enemy, enemy.enemyMovementDirection(player))
                            }
                        }
                        12-> {
                            move(enemy, enemy.enemyMovementDirection(player))
                        }
                    }
                }
                enemyTurn++
            }
        }
        return Solution(isClear, turn, player, routeArray)
    }

    /**
     * ga
     */
    fun exploration(route: ArrayList<Pair<Int, Int>>, level: Int): Solution {
        gameInit(level)
        var solution = routeValidation(route)
        if(!solution.clear && solution.player.hp>0){
            solution = randomSearch()
        }
        return solution
    }

    /**
     * aco
     */
    fun ant(pheromoneGrid: Array<Array<Float>>, wPh: Float, wDt: Float, wFd: Float, wEn: Float): Solution{
        while(playerTurn){
            val possibleDirection = player.possiblePlayerMovementDirection()
            var direction = Pair(0, 0)
            var totalWeight = 0.0
            val weightArray = ArrayList<Float>()
            val weightIndex = ArrayList<Pair<Int, Int>>()

            run loop@{

                possibleDirection.forEach {
                    val toX = player.x+it.first
                    val toY = player.y+it.second

                    val toObject = gridPosition[toX][toY]
                    when(toObject.type){
                        11,12,112->return@forEach
                        100,2-> {
                            direction = it
                            return@loop
                        }
                    }

                    val pheromone = pheromoneGrid[toX][toY]
                    val distance = (columns-toX-1) + (rows-toY-1)

                    //val weight = pheromone.pow(wPh)*(1f/distance).pow(wDt)*(1/food).pow(wFd)*(enemy).pow(wEn)
                    val weight = pheromone.pow(wPh)*(1f/distance).pow(wDt)
                    totalWeight += weight
                    weightArray.add(weight)
                    weightIndex.add(it)
                }

                run loop2@{
                    var ran = Math.random()
                    weightArray.forEachIndexed { index, weight ->
                        ran -= (weight/totalWeight)
                        if(ran < 0) {
                            direction = weightIndex[index]
                            return@loop2
                        }
                    }
                }
            }
            move(player, direction)

            enemyArray.forEach { enemy ->
                when(enemy.type){
                    11->{
                        if (enemyTurn % 2 == 0) {
                            move(enemy, enemy.enemyMovementDirection(player))
                        }
                    }
                    12-> {
                        move(enemy, enemy.enemyMovementDirection(player))
                    }
                }
            }
            enemyTurn++

        }
        val solution = Solution(isClear, turn, player, routeArray)
        solution.positionHistory = historyArray
        return solution
    }

    /**
     * greedy
     */
    fun shortest(): Solution {
        while(playerTurn){
            val possibleDirection = player.possiblePlayerMovementDirection()
            var potential = 0
            var direction = Pair(0, 0)
            run loop@{
                possibleDirection.forEach {
                    val toX = player.x+it.first
                    val toY = player.y+it.second
                    val toObject = gridPosition[toX][toY]
                    when(toObject.type){
                        0,1 -> {
                            if(toX+toY > potential){
                                potential = toX+toY
                                direction = it
                            }else if(toX+toY == potential){
                                val ran = (0 until 2).random()
                                if(ran == 0){
                                    potential = toX+toY
                                    direction = it
                                }
                            }
                        }
                        100,2 -> {
                            direction = it
                            return@loop
                        }
                    }
                }
            }

            move(player, direction)

            enemyArray.forEach { enemy ->
                when(enemy.type){
                    11->{
                        if (enemyTurn % 2 == 0) {
                            move(enemy, enemy.enemyMovementDirection(player))
                        }
                    }
                    12-> {
                        move(enemy, enemy.enemyMovementDirection(player))
                    }
                }
            }
            enemyTurn++
        }

        return Solution(isClear, turn, player, routeArray)

    }


    private fun move(gameObject: GameObject, direction: Pair<Int, Int>){
        val fromX = gameObject.x
        val fromY = gameObject.y
        val toX = gameObject.x+direction.first
        val toY = gameObject.y+direction.second
        when(gameObject.type) {
            10 -> { //player
                turn++
                routeArray.add(direction)

                val playerHit = gameObject.hit(1)
                if(playerHit<=0){
                    gameOver()
                    return
                }

                if(toX>=0 && toX<=columns-1 && toY>=0 && toY<=rows-1){
                    val toObject = gridPosition[toX][toY]
                    when(toObject.type){
                        0 -> {
                            gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                            gameObject.x = toX
                            gameObject.y = toY
                            gridPosition[toX][toY] = gameObject
                            //println("player move : ${gameObject.hp}(${gameObject.x}, ${gameObject.y})")
                        }
                        1 -> {
                            val wallHit = gridPosition[toX][toY].hit(1)
                            if(wallHit<=0){
                                gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                                gameObject.x = toX
                                gameObject.y = toY
                                gridPosition[toX][toY] = gameObject
                            }
                            //println("hit wall $wallHit")
                        }
                        2 -> {
                            gameObject.hit(-(gridPosition[toX][toY].hp))
                            gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                            gameObject.x = toX
                            gameObject.y = toY
                            gridPosition[toX][toY] = gameObject
                            //println("eat food : ${player.hp}(${player.x}, ${player.y})")
                        }
                        100 -> {
                            gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                            gameObject.x = toX
                            gameObject.y = toY
                            gridPosition[toX][toY] = gameObject
                            isClear = true
                            gameOver()
                            return
                            //println("clear")
                        }
                    }
                }
                historyArray.add(Pair(gameObject.x, gameObject.y))
            }
            11,12 -> {  //enemy
                val toObject = gridPosition[toX][toY]
                when(toObject.type){
                    0 -> {
                        when(gridPosition[fromX][fromY].type){
                            11,12 -> {
                                gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                                gameObject.x = toX
                                gameObject.y = toY
                                gridPosition[toX][toY] = gameObject
                            }
                            112 -> {
                                val food = GameObject(2, fromX, fromY)
                                food.hp = 20
                                gridPosition[fromX][fromY] = food
                                gameObject.x = toX
                                gameObject.y = toY
                                gridPosition[toX][toY] = gameObject
                            }
                        }
                        //println("enemy move : ${gameObject.hp}(${gameObject.x}, ${gameObject.y})")
                    }
                    2 -> {
                        gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                        gameObject.x = toX
                        gameObject.y = toY
                        gridPosition[toX][toY] = GameObject(112, fromX, fromY)
                    }
                    10 -> {
                        val playerHit = player.hit(gameObject.damage)
                        //println("hit player $playerHit")
                        if(playerHit<=0){
                            gameOver()
                            return
                        }
                    }
                }
            }
        }
    }

    private fun gameOver() {
        playerTurn = false
    }

}
