package main

class StageMaker {

    companion object {
        private const val MUTATION_RATE = 0.05f
        private const val POPULATION = 100
        private const val GENERATION = 20
    }

    private fun makePopulation(columns: Int) : ArrayList<Stage> {
        val result = ArrayList<Stage>()
        repeat(POPULATION){
            result.add(GameBoard(columns).randomGrid())
        }
        return result
    }

    private fun crossover(parent1: Stage, parent2: Stage) : Pair<Stage, Stage> {
        val columns = parent1.columns
        val rows = parent1.rows
        val ran = (0 until columns).random()
        val grid1 = Array(columns){ Array(rows) { GameObject() } }
        val grid2 = Array(columns){ Array(rows) { GameObject() } }

        val oCount = arrayListOf(0,0,0,0,0)

        for(i in 0 until ran){
            grid1[i].forEachIndexed { index, gameObject ->
                when(gameObject.type){
                    1->oCount[0]--
                    2->oCount[1]--
                    3->oCount[2]--
                    11->oCount[3]--
                    12->oCount[4]--
                }
                grid1[i][index] = GameObject(parent1.gridPosition[i][index])
                when(parent1.gridPosition[i][index].type){
                    1->oCount[0]++
                    2->oCount[1]++
                    3->oCount[2]++
                    11->oCount[3]++
                    12->oCount[4]++
                }
            }
            grid2[i].forEachIndexed { index, _ ->
                grid2[i][index] = GameObject(parent2.gridPosition[i][index])
            }
        }
        for(i in ran until columns){
            grid1[i].forEachIndexed { index, gameObject ->
                when(gameObject.type){
                    1->oCount[0]--
                    2->oCount[1]--
                    3->oCount[2]--
                    11->oCount[3]--
                    12->oCount[4]--
                }
                grid1[i][index] = GameObject(parent2.gridPosition[i][index])
                when(parent1.gridPosition[i][index].type){
                    1->oCount[0]++
                    2->oCount[1]++
                    3->oCount[2]++
                    11->oCount[3]++
                    12->oCount[4]++
                }
            }
            grid2[i].forEachIndexed { index, _ ->
                grid2[i][index] = GameObject(parent1.gridPosition[i][index])
            }
        }

        val o1 = Stage(columns, parent1.wallCount+oCount[0], parent1.food1Count+oCount[1],
            parent1.food2Count+oCount[2], parent1.enemy1Count+oCount[3], parent1.enemy2Count+oCount[4])
        o1.addGridPosition(grid1)

        val o2 = Stage(columns, parent2.wallCount-oCount[0], parent2.food1Count-oCount[1],
            parent2.food2Count-oCount[2], parent2.enemy1Count-oCount[3], parent2.enemy2Count-oCount[4])
        o2.addGridPosition(grid2)

        return Pair(o1, o2)
    }

    private fun evaluation(stageArray: ArrayList<Stage>) : ArrayList<Stage> {

        return stageArray
    }

    private fun selection(stageArray: ArrayList<Stage>): Pair<Stage, Stage> {
        val candidates = ArrayList<Stage>()

        for(i in 0 until stageArray.size/10) {
            val ran = (0 until stageArray.size).random()
            candidates.add(stageArray[ran])
        }

        evaluation(candidates)

        return Pair(candidates[0], candidates[1])
    }

    private fun mutate(stage: Stage): Stage {
        stage.gridPosition.forEachIndexed { i, arrayOfGameObjects ->
            arrayOfGameObjects.forEachIndexed { j, _ ->
                val ran = (0 until 100).random()
                if(ran< MUTATION_RATE*100) {
                    val ran1 = (0 until stage.columns).random()
                    val ran2 = (0 until stage.rows).random()
                    val tmp = GameObject(stage.gridPosition[i][j])
                    stage.gridPosition[i][j] = GameObject(stage.gridPosition[ran1][ran2])
                    stage.gridPosition[ran1][ran2] = tmp
                }
            }
        }

        return stage
    }

    fun ga(columns: Int): ArrayList<Stage> {
        val populationArray = makePopulation(columns)
        repeat(GENERATION) {
            val offspringArray = ArrayList<Stage>()
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
            repeat(POPULATION){ //토너먼트 선택
                populationArray.removeLast()
            }
        }
        return populationArray  //솔루션
    }
}