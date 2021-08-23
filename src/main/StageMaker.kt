package main

class StageMaker {

    companion object {

        private var playerTurn = true
        private var enemyTurn = 0

        private var turn = 0
        private var routeArray = ArrayList<Pair<Int, Int>>()
        private var historyArray = ArrayList<Pair<Int, Int>>()
        private var isClear = false

        @Volatile private var instance: StageMaker? = null
        @JvmStatic fun getInstance(): StageMaker =
            instance ?: synchronized(this) {
                instance ?: StageMaker().also {
                    instance = it
                }
            }
    }


}