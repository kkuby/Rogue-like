package agent

import main.GameManager
import main.Solution

class Greedy {
    private val gameManager = GameManager.getInstance()
    fun greedy(population: Int, generation: Int, level: Int) : ArrayList<Solution> {
        val solutionArray = ArrayList<Solution>()
        repeat(population){
            gameManager.gameInit(level)
            solutionArray.add(gameManager.shortest())
        }
        repeat(generation-1){
            repeat(population){
                gameManager.gameInit(level)
                solutionArray.add(gameManager.shortest())
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