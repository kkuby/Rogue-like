package agent

import main.GameManager
import main.GameObject
import main.Solution

class ACO {
    companion object {
        private const val INIT_PHEROMONE = 10f
        private const val WEIGHT_PHEROMONE = 1.5f
        private const val WEIGHT_DISTANCE = 2f
        private const val WEIGHT_FOOD = 0.5f
        private const val WEIGHT_ENEMY = 1f
        private const val EVAPORATION_GLOBAL_RATE = 0.1f
        private const val ESTIMATED_SHORTEST_TOUR = 20f

        private val gameManager = GameManager.getInstance()

        var pheromoneGrid = Array(gameManager.columns){ Array(gameManager.rows) { INIT_PHEROMONE } }

        @Volatile private var instance: ACO? = null
        @JvmStatic fun getInstance(): ACO =
            instance ?: synchronized(this) {
                instance ?: ACO().also {
                    instance = it
                }
            }
    }

    private fun updateGlobalPheromone(solution: Solution) {

        //evaporation
        pheromoneGrid.forEachIndexed { i, array ->
            array.forEachIndexed { j, _ ->
                pheromoneGrid[i][j] *= (1- EVAPORATION_GLOBAL_RATE)
            }
        }

        //pheromone deposit
        solution.positionHistory.forEach {
            val x = it.first
            val y = it.second

            if(solution.clear){
                pheromoneGrid[x][y] += (ESTIMATED_SHORTEST_TOUR/solution.turn)
            }
        }
    }

    fun aco(population: Int, generation: Int, level: Int) : ArrayList<Solution> {
        val solutionArray = ArrayList<Solution>()
        repeat(population) {
            gameManager.gameInit(level)
            val solution = gameManager.ant(pheromoneGrid, WEIGHT_PHEROMONE, WEIGHT_DISTANCE, WEIGHT_FOOD, WEIGHT_ENEMY)
            updateGlobalPheromone(solution)
            solutionArray.add(solution)
        }

        repeat(generation-1){
            repeat(population){
                gameManager.gameInit(level)
                val solution = gameManager.ant(pheromoneGrid, WEIGHT_PHEROMONE, WEIGHT_DISTANCE, WEIGHT_FOOD, WEIGHT_ENEMY)
                updateGlobalPheromone(solution)
                solutionArray.add(solution)
            }

            solutionArray.sortByDescending {
                it.player.hp
            }

            solutionArray.sortBy {
                !it.clear
            }

            repeat(population){
                solutionArray.removeLast()
            }

        }

        return solutionArray
    }
}