package main

class Solution(val clear: Boolean, val turn: Int, val player: GameObject, val route: List<Pair<Int, Int>>){

    var positionHistory = ArrayList<Pair<Int, Int>>()

    fun path() {
        var path = ""
        route.forEachIndexed { index, pair ->
            when(pair){
                Pair(-1,0) -> path+="1"
                Pair(1,0) -> path+="2"
                Pair(0,1) -> path+="3"
                Pair(0,-1) -> path+="4"
            }
            if(index != route.size-1){
                path+=","
            }
        }
        println(path)
    }

    fun result() {
        println("$clear, $turn, ${player.hp}(${player.x}, ${player.y})")
    }
}
