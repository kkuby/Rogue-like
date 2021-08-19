package main
//type 0: floor, 1: wall, 2: food, 10: player, 11: enemy1, 12: enemy2, 100: exit, 112: enemy+food
class GameObject(var type: Int, var x: Int, var y: Int){
    var hp = 0
    var damage = 0

    constructor(gameObject: GameObject) : this(gameObject.type, gameObject.x, gameObject.y){
        hp = gameObject.hp
        damage = gameObject.damage
    }

    fun hit(loss: Int): Int {
        hp -= loss
        return hp
    }

    fun enemyMovementDirection(player: GameObject): Pair<Int, Int> {
        var xDirection = 0
        var yDirection = 0

        if(player.x == x) {
            yDirection = if (player.y > y) 1 else -1
        }else{
            xDirection = if(player.x > x) 1 else -1
        }
        return Pair(xDirection, yDirection)
    }

    fun randomPlayerMovementDirection(): Pair<Int, Int> {
        val pairArray = ArrayList<Pair<Int, Int>>()
        pairArray.add(Pair(-1, 0))
        pairArray.add(Pair(1, 0))
        pairArray.add(Pair(0, -1))
        pairArray.add(Pair(0, 1))

        val ran = (0 until pairArray.size).random()

        return pairArray[ran]
    }

    fun possiblePlayerMovementDirection(): ArrayList<Pair<Int, Int>> {
        val pairArray = ArrayList<Pair<Int, Int>>()
        if(x>0){
            pairArray.add(Pair(-1, 0))
        }
        if(x<GameManager.getInstance().columns-1){
            pairArray.add(Pair(1, 0))
        }
        if(y>0){
            pairArray.add(Pair(0, -1))
        }
        if(y<7){
            pairArray.add(Pair(0, 1))
        }

        return pairArray
    }

}