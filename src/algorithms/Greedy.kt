package algorithms

import main.GameManager
import main.Solution

class Greedy {

    fun shortest() : Solution{

    }

    fun evaluation(solutionArray: ArrayList<Solution>) : {

    }

    fun greedy(population: Int, generation: Int) : ArrayList<Solution> {
        val solutionArray = ArrayList<Solution>()
        repeat(population){ //최초 population 생성
            solutionArray.add(shortest())   //최단 경로
        }

        repeat(generation-1){   //generation-1 만큼 반복
            repeat(population){ //population 만큼 반복
                solutionArray.add(shortest())   //최단 경로
            }
            evaluation(solutionArray)   //우수한 순서대로 정렬
            repeat(population){ //토너먼트 선택
                solutionArray.removeLast()
            }
        }

        return solutionArray
    }

}