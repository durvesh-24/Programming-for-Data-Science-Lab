//> using scala 2.12.15

// Week 12 | Program S. No. 21 | Exercise Program
// Topic : Exploring advanced functional combinators beyond the basic set
// Problem: Create a list of integers from 1 to 10, then use filter to create a new list
// with only even numbers. Use map to create a new list where each element is multiplied
// by 3. Use flatMap to create a list of tuples where each integer is paired with its
// square. Finally, use foldLeft to calculate the product of all elements in the list.

object FunctionalCombinators {

  def main(args: Array[String]): Unit = {

    // Step 1: Create a list of integers from 1 to 10
    val numbers: List[Int] = (1 to 10).toList
    println("Original list            : " + numbers)

    // Step 2: filter - keep only the even numbers
    val evens = numbers.filter(n => n % 2 == 0)
    println("filter (even numbers)    : " + evens)

    // Step 3: map - multiply every element by 3
    val tripled = numbers.map(n => n * 3)
    println("map (each * 3)           : " + tripled)

    // Step 4: flatMap - pair every integer with its square
    val pairs: List[(Int, Int)] = numbers.flatMap(n => List((n, n * n)))
    println("flatMap (n, n^2)         : " + pairs)

    // Step 5: foldLeft - product of all the elements (Long avoids overflow)
    val product = numbers.foldLeft(1L)((acc, n) => acc * n)
    println("foldLeft (product)       : " + product)

    // Step 6: Other combinators beyond the basic set
    println("\nAdditional combinators:")
    println("reduce (sum)             : " + numbers.reduce(_ + _))
    println("scanLeft (running sum)   : " + numbers.scanLeft(0)(_ + _))
    println("partition (even, odd)    : " + numbers.partition(_ % 2 == 0))
    println("collect (odd squares)    : " + numbers.collect { case n if n % 2 != 0 => n * n })
    println("takeWhile (< 5)          : " + numbers.takeWhile(_ < 5))
    println("dropWhile (< 5)          : " + numbers.dropWhile(_ < 5))
    println("groupBy (n % 3)          : " + numbers.groupBy(_ % 3).toList.sortBy(_._1))
    println("zipWithIndex (first 4)   : " + numbers.zipWithIndex.take(4))
    println("grouped (size 3)         : " + numbers.grouped(3).toList)
    println("sliding (window 3)       : " + numbers.sliding(3).take(3).toList)
    println("exists (> 9)             : " + numbers.exists(_ > 9))
    println("forall (> 0)             : " + numbers.forall(_ > 0))

    // Step 7: Chaining combinators - sum of squares of even numbers
    val sumOfEvenSquares = numbers.filter(_ % 2 == 0).map(n => n * n).foldLeft(0)(_ + _)
    println("\nSum of squares of evens  : " + sumOfEvenSquares)
  }
}
