//> using scala 2.12.15

// Week 9 | Program S. No. 18 | Exercise Program
// Topic : Arrays in Scala
// Problem: Create an array of integers with the values 2, 5, 9, 14, 20. Write a function
// that takes this array and returns the sum of its elements. Next, create an array of
// strings with the names of five different fruits. Write a function that concatenates
// all elements of this array, separated by commas. Finally, iterate over the array of
// integers and print each element to the console.

object ArraysInScala {

  // Step 2: Function that returns the sum of all elements of an integer array
  def sumOfElements(numbers: Array[Int]): Int = {
    var total = 0
    for (n <- numbers) {
      total += n
    }
    total
  }

  // Step 4: Function that joins all fruit names separated by commas
  def concatenateWithCommas(items: Array[String]): String = {
    items.mkString(", ")
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Create an array of integers
    val numbers: Array[Int] = Array(2, 5, 9, 14, 20)
    println("Integer Array      : " + numbers.mkString("[", ", ", "]"))
    println("Length of Array    : " + numbers.length)

    // Step 2: Sum of the elements using the user-defined function
    val total = sumOfElements(numbers)
    println("Sum of Elements    : " + total)
    println("Sum using .sum     : " + numbers.sum)

    // Step 3: Create an array of strings with five fruit names
    val fruits: Array[String] = Array("Apple", "Banana", "Mango", "Orange", "Grapes")
    println("\nFruit Array        : " + fruits.mkString("[", ", ", "]"))

    // Step 4: Concatenate the fruit names separated by commas
    val fruitString = concatenateWithCommas(fruits)
    println("Concatenated Fruits: " + fruitString)

    // Step 5: Iterate over the integer array and print each element
    println("\nElements of the integer array:")
    for (i <- numbers.indices) {
      println(s"numbers($i) = ${numbers(i)}")
    }

    // Step 6: Iterate using foreach
    println("\nElements printed using foreach:")
    numbers.foreach(n => println(n))
  }
}
