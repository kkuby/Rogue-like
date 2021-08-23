package algorithms

import main.GameManager
import main.GameObject
import main.Solution

class ACO {
    private var INIT_PHEROMONE = 10f
    private var WEIGHT_PHEROMONE = 1.5f
    private var WEIGHT_DISTANCE = 2f
    private var WEIGHT_FOOD = 0.5f
    private var WEIGHT_ENEMY = 1f
    private var EVAPORATION_GLOBAL_RATE = 0.1f
    private var ESTIMATED_SHORTEST_TOUR = 20f

    lateinit var pheromoneGrid: Array<Array<Float>>

    private fun init(columns: Int, rows: Int) {
        INIT_PHEROMONE = 10f    //초기 페로몬 양
        WEIGHT_PHEROMONE = 1.5f //페로몬 가중치
        WEIGHT_DISTANCE = 2f    //거리 가중치
        WEIGHT_FOOD = 0.5f  //음식 가중치
        WEIGHT_ENEMY = 1f   //몬스터 가중치
        EVAPORATION_GLOBAL_RATE = 0.1f  //페로몬 증발률
        ESTIMATED_SHORTEST_TOUR = ((columns-1) + (rows-1)).toFloat()   //최단 거리 추정치
        pheromoneGrid = Array(columns){ Array(rows) { INIT_PHEROMONE } }    //페로몬 지도
    }

    fun updateGlobalPheromone(solution: Solution) {
        pheromoneGrid.forEachIndexed { i, array ->  //전체 페로몬 지도 evaporation
            array.forEachIndexed { j, _ ->
                pheromoneGrid[i][j] *= (1- EVAPORATION_GLOBAL_RATE)
            }
        }
        solution.positionHistory.forEach {  //개미의 이동 경로 pheromone deposit
            val x = it.first
            val y = it.second

            if(solution.clear){
                pheromoneGrid[x][y] += (ESTIMATED_SHORTEST_TOUR/solution.turn)
            }
        }
    }

    fun moveAnt() : Solution {

    }

    fun evaluation(solutionArray : ArrayList<Solution>){

    }

    fun aco(population: Int, generation: Int, columns: Int, rows: Int) : ArrayList<Solution> {
        init(columns, rows) //가중치 및 페로몬 초기화
        val solutionArray = ArrayList<Solution>()
        val antArray = ArrayList<Ant>()
        repeat(population){ //population 수 만큼 개미 생성
            Thread {    //동시에 여행 시작
                val ant = Ant("$it", generation)    //개미는 generation 수 만큼 여행 반복
                ant.startSearch()
                antArray.add(ant)
            }
        }
        var antComplete = 0 //여행 완료 한 개미 수
        while(antArray.size > antComplete){ //모든 개미가 여행을 완료 할 때 까지
            antArray.forEach {
                if(it.complete){
                    antComplete++
                    solutionArray.add(it.solutionArray[0])  //각 개미의 가장 우수한 솔루션
                }
            }
        }
        evaluation(solutionArray)   //각 개미의 가장 우수한 솔루션을 다시 토너먼트 선택

        return solutionArray
    }

    class Ant(private val id: String, private val generation: Int) {
        private val aco = ACO()
        val solutionArray = ArrayList<Solution>()
        var complete = false
        fun startSearch() { //개미 여행 시작
            repeat(generation){ //generation 만큼 반복
                val solution = aco.moveAnt()    //가중치에 따른 확률로 개미 이동
                aco.updateGlobalPheromone(solution) //여행 마치면 페로몬 업데이트
                solutionArray.add(solution)
            }
            aco.evaluation(solutionArray)   //우수한 순서대로 정렬
            complete = true
        }
    }
}
