//> using scala 2.12.15

// Week 10 | Program S. No. 19 | Exercise Program
// Topic : Lists, Sets, and Tuples in Scala
// Problem: Create a list of the first five prime numbers. Write a function that takes this
// list and returns a new list with each element squared. Then, create a set of unique
// characters from the string "hello world" and write a function that takes two sets and
// returns their intersection. Create a tuple with three elements: an integer, a string,
// and a boolean, then access and print each element.

object ListsSetsTuples {

  // Step 2: Function that returns a new list with each element squared
  def squareList(numbers: List[Int]): List[Int] = {
    numbers.map(n => n * n)
  }

  // Step 4: Function that returns the intersection of two sets
  def intersection[A](set1: Set[A], set2: Set[A]): Set[A] = {
    set1.intersect(set2)
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Create a list of the first five prime numbers
    val primes: List[Int] = List(2, 3, 5, 7, 11)
    println("List of first five primes : " + primes)

    // Step 2: Square every element of the list
    val squaredPrimes = squareList(primes)
    println("Squared primes            : " + squaredPrimes)
    println("Original list (unchanged) : " + primes)

    // Step 3: Create a set of unique characters from "hello world"
    val text = "hello world"
    val uniqueChars: Set[Char] = text.filter(_ != ' ').toSet
    println("\nString                    : \"" + text + "\"")
    println("Unique characters (Set)   : " + uniqueChars)
    println("Number of unique chars    : " + uniqueChars.size)

    // Step 4: Intersection of two sets
    val otherChars: Set[Char] = "scala world".filter(_ != ' ').toSet
    println("Second set (\"scala world\"): " + otherChars)
    println("Intersection of the sets  : " + intersection(uniqueChars, otherChars))

    val setA = Set(1, 2, 3, 4, 5)
    val setB = Set(4, 5, 6, 7)
    println(s"Intersection of $setA and $setB = ${intersection(setA, setB)}")

    // Step 5: Create a tuple with an Int, a String and a Boolean
    val record: (Int, String, Boolean) = (26, "Durvesh", true)
    println("\nTuple                     : " + record)

    // Step 6: Access and print each element of the tuple
    println("First element  (Int)      : " + record._1)
    println("Second element (String)   : " + record._2)
    println("Third element  (Boolean)  : " + record._3)

    // Step 7: Access the elements using pattern matching
    val (number, name, flag) = record
    println(s"Using pattern matching    : number = $number, name = $name, flag = $flag")
  }
}
