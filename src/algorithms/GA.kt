package algorithms

import main.GameManager
import main.Solution

class GA {

    companion object {
        private const val MUTATION_RATE = 0.05f
    }

    private fun makePopulation(population: Int) : ArrayList<Solution> {
        val result = ArrayList<Solution>()
        repeat(population){
            gameManager.gameInit(level)
            result.add(gameManager.randomSearch())
        }
        return result
    }

    private fun crossover(parent1: Solution, parent2: Solution): Pair<Solution, Solution> {
        val ran1 = (0 until parent1.route.size).random()
        val ran2 = (0 until parent2.route.size).random()
        val route1 = ArrayList<Pair<Int, Int>>()
        val route2 = ArrayList<Pair<Int, Int>>()

        route1.addAll(parent1.route.subList(0, ran1))
        route1.addAll(parent2.route.subList(ran2, parent2.route.size))
        route2.addAll(parent2.route.subList(0, ran2))
        route2.addAll(parent1.route.subList(ran1, parent1.route.size))

        val o1 = gameManager.exploration(route1, level)
        val o2 = gameManager.exploration(route2, level)

        return Pair(o1, o2)
    }

    private fun evaluation(solutionArray: ArrayList<Solution>) :ArrayList<Solution> {
        solutionArray.sortByDescending {
            it.player.hp
        }
        solutionArray.sortBy {
            !it.clear
        }

        return solutionArray
    }

    private fun selection(solutionArray: ArrayList<Solution>): Pair<Solution, Solution> {
        val candidates = ArrayList<Solution>()

        for(i in 0 until solutionArray.size/10) {
            val ran = (0 until solutionArray.size).random()
            candidates.add(solutionArray[ran])
        }

        evaluation(candidates)

        return Pair(candidates[0], candidates[1])
    }

    private fun mutator(pair: Pair<Int, Int>): Pair<Int, Int> {
        val direction = ArrayList<Pair<Int, Int>>()
        direction.add(Pair(-1, 0))
        direction.add(Pair(1, 0))
        direction.add(Pair(0, 1))
        direction.add(Pair(0, -1))
        var ran = (0 until direction.size).random()
        while(pair==direction[ran]){
            ran = (0 until direction.size).random()
        }
        return direction[ran]
    }

    private fun mutate(solution: Solution): Solution {
        val route = ArrayList<Pair<Int, Int>>()
        route.addAll(solution.route)
        route.forEachIndexed { index, pair ->
            val ran = (0 until 100).random()
            if(ran<MUTATION_RATE*100){
                route[index] = mutator(pair)
            }
        }

        return gameManager.exploration(route, level)
    }

    fun ga(population: Int, generation: Int) : ArrayList<Solution> {
        val populationArray = makePopulation(population)    //population 수 만큼 랜덤 솔루션 생성
        repeat(generation) {    //generation 수 만큼 반복
            val offspringArray = ArrayList<Solution>()  //자손 배열
            while(populationArray.size > offspringArray.size) { //자손이 부모 만큼 생성 될 때 까지
                val (p1, p2) = selection(populationArray)   //우수한 부모 토너먼트 선택
                var (o1, o2) = crossover(p1, p2)    //부모의 교차를 통한 자손 생성
                o1 = mutate(o1) //자손 변이
                o2 = mutate(o2)
                offspringArray.add(o1)
                offspringArray.add(o2)
            }
            populationArray.addAll(offspringArray)
            evaluation(populationArray) //우수한 순서대로 정렬
            repeat(population){ //토너먼트 선택
                populationArray.removeLast()
            }

        }
        return populationArray  //솔루션
    }

}