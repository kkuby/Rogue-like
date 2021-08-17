
private const val level = 3
private var playerTurn = true

val gameBoard = GameBoard()
val gridPosition = gameBoard.gridPosition
val player = gameBoard.player
val enemyArray = gameBoard.enemyArray

private var turn = 0
private val rootArray = ArrayList<Pair<Int, Int>>()
private var isClear = false

fun main(args: Array<String>) {
    gameBoard.setupScene(level)

    while(playerTurn){
        move(player, player.playerMovementDirection())

        enemyArray.forEach {
            move(it, it.enemyMovementDirection(player))
        }
    }

}

fun move(gameObject: GameObject, direction: Pair<Int, Int>){
    val fromX = gameObject.x
    val fromY = gameObject.y
    val toX = gameObject.x+direction.first
    val toY = gameObject.y+direction.second
    val toObject = gridPosition[toX][toY]
    when(gameObject.type) {
        10 -> { //player
            turn++
            val playerHit = gameObject.hit(1)
            if(playerHit<=0){
                gameOver()
            }
            when(toObject.type){
                0 -> {
                    gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                    gameObject.x = toX
                    gameObject.y = toY
                    gridPosition[toX][toY] = gameObject
                    rootArray.add(direction)
                    println("player move : ${gameObject.hp}(${gameObject.x}, ${gameObject.y})")
                }
                1 -> {
                    val wallHit = gridPosition[toX][toY].hit(1)
                    if(wallHit<=0){
                        gridPosition[toX][toY] = GameObject(0, toX, toY)
                    }
                    println("hit wall $wallHit")
                }
                2 -> {
                    gameObject.hit(-(gridPosition[toX][toY].hp))
                    gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                    gameObject.x = toX
                    gameObject.y = toY
                    gridPosition[toX][toY] = gameObject
                    rootArray.add(direction)
                    println("eat food : ${player.hp}(${player.x}, ${player.y})")
                }
                100 -> {
                    gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                    gameObject.x = toX
                    gameObject.y = toY
                    gridPosition[toX][toY] = gameObject
                    rootArray.add(direction)
                    isClear = true
                    gameOver()
                    println("clear")
                }
            }
        }
        11,12 -> {  //enemy
            when(toObject.type){
                0 -> {
                    gridPosition[fromX][fromY] = GameObject(0, fromX, fromY)
                    gameObject.x = toX
                    gameObject.y = toY
                    gridPosition[toX][toY] = gameObject
                    println("enemy move : ${gameObject.hp}(${gameObject.x}, ${gameObject.y})")
                }
                10 -> {
                    val playerHit = player.hit(gameObject.damage)
                    println("hit player $playerHit")
                    if(playerHit<=0){
                        gameOver()
                    }
                }
            }
        }
    }
}

fun gameOver() {
    playerTurn = false
    println("clear : $isClear, turn : $turn, player : ${player.hp}(${player.x}, ${player.y})")
    rootArray.forEach {
        print("(${it.first}, ${it.second})")
    }
}
