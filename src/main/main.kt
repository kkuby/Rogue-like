import algorithms.ACO
import algorithms.GA
import algorithms.Greedy
import main.GameManager
import main.GameObject
import main.Solution
import main.StageMaker

/**
 * same result validation
 */
fun sameResultValidation(){
    gameManager.gameInit(LEVEL)
    var solution = gameManager.randomSearch()
    solution.result()
    gameManager.gameInit(LEVEL)
    solution = gameManager.routeValidation(solution.route)
    solution.result()
    solution.path()
}

/**
 * path validation
 */
fun pathValidation(path: String) {
    val route = ArrayList<Pair<Int, Int>>()
    val directionArray = path.split(",")
    directionArray.forEach {
        when(it){
            "1"->route.add(Pair(-1, 0))
            "2"->route.add(Pair(1, 0))
            "3"->route.add(Pair(0, 1))
            "4"->route.add(Pair(0, -1))
        }
    }

    gameManager.gameInit(LEVEL)
    val solution = gameManager.routeValidation(route)
    solution.result()
    solution.path()
}

/**
 * map validation
 */
fun mapValidation(map: String, path: String) {
    val mapArray = map.split(" ")
    val columns = mapArray.size/8
    val rows = 8
    val gridPosition = Array(columns){ Array(rows) { GameObject(-1,-1,-1) } }
    repeat(columns){ x ->
        repeat(rows){ y ->
            gridPosition[x][y] = GameObject(0, x, y)
        }
    }

    val player = GameObject(10, 0, 0)
    player.hp = 100
    val exit = GameObject(100, columns-1, rows-1)
    gridPosition[0][0] = player
    gridPosition[columns-1][rows-1] = exit

    val enemies = ArrayList<GameObject>()
    val foods = ArrayList<GameObject>()

    mapArray.forEachIndexed { index, s ->
        val x = index/8
        val y = index%8
        when(s){
            "1"->{
                val wall = GameObject(1, x, y)
                wall.hp = 3
                gridPosition[x][y] = wall
            }
            "2"->{
                val food = GameObject(2, x, y)
                food.hp = 20
                gridPosition[x][y] = food
                foods.add(food)
            }
            "11"->{
                val enemy1 = GameObject(11, x, y)
                enemy1.damage = 10
                gridPosition[x][y] = enemy1
                enemies.add(enemy1)
            }
            "12"->{
                val enemy2 = GameObject(12, x, y)
                enemy2.damage = 20
                gridPosition[x][y] = enemy2
                enemies.add(enemy2)
            }
        }
    }

    gameManager.gameInit(-1)
    gameManager.setGrid(gridPosition, player, enemies, foods)

    pathValidation(path)

}

/**
 * random game
 */
fun randomGame() : ArrayList<Solution> {
    val solutionArray = ArrayList<Solution>()
    repeat(POPULATION){
        gameManager.gameInit(LEVEL)
        solutionArray.add(gameManager.randomSearch())
    }
    repeat(GENERATION-1) {
        repeat(POPULATION){
            gameManager.gameInit(LEVEL)
            solutionArray.add(gameManager.randomSearch())
        }
        solutionArray.sortByDescending {
            it.player.hp
        }

        solutionArray.sortBy {
            !it.clear
        }

        repeat(POPULATION){
            solutionArray.removeLast()
        }

    }

    return solutionArray
}

fun evaluation(solutionArray: ArrayList<Solution>) {
    val count = solutionArray.size
    var clearCount = 0
    var totalHP = 0
    var totalTurn = 0
    solutionArray.forEach {
        if(it.clear){
            clearCount++
            totalHP+=it.player.hp
            totalTurn+=it.turn
        }
    }

    if(clearCount == 0){
        println("fail")
    }else{
        println("clearRate : $clearCount/$count, avgHP : ${totalHP/clearCount}, avgTurn : ${totalTurn/clearCount}")
    }
    solutionArray[0].result()
    solutionArray[0].path()

}

val gameManager = GameManager.getInstance()
val stageMaker = StageMaker.getInstance()

const val POPULATION = 80
const val GENERATION = 20
const val LEVEL = 5
fun main(args: Array<String>) {

    //sameResultValidation()
    //pathValidation("2,3,3,2,2,2,2,2,2,2,2,3,3,1,3,3,3,3,1,3,2,2")

    val ga = GA()
    ga.ga(POPULATION, GENERATION)
    //evaluation(ga.ga(POPULATION, GENERATION, LEVEL))
//    evaluation(randomGame())
    val greedy = Greedy()
    evaluation(greedy.greedy(POPULATION, GENERATION, LEVEL))

    val aco = ACO()
    aco.aco(POPULATION, GENERATION, 14, 8)
    //evaluation(aco.aco(POPULATION, GENERATION, LEVEL))

    //make map
    //evaluation(ga.ga(POPULATION, GENERATION, -1))

//    mapValidation("10 0 2 0 11 0 0 11 0 0 1 0 0 0 0 0 11 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 11 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 11 0 11 0 0 0 0 2 0 0 0 0 0 0 0 1 0 0 0 0 2 0 0 0 0 0 0 11 0 0 0 0 0 0 11 0 0 0 0 0 2 0 0 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 100",
//    "2,2,4,3,3,4,3,1,2,3,2,3,2,1,1,2,3,2,2,2")
}