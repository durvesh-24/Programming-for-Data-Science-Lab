//> using scala 2.12.15

// Week 13 | Program S. No. 22 | Exercise Program
// Topic : Case Classes
// Problem: Create a case class Person with fields name, age, and city. Create a list of
// Person objects representing five different individuals. Write a function to filter out
// people older than 30. Next, write a function that groups people by their city. Then,
// write a function that transforms each Person object into a string in the format
// "Name is Age years old and lives in City". Finally, write a function to sort the list
// of people by age in ascending order.

// Step 1: Define the case class
case class Person(name: String, age: Int, city: String)

object CaseClasses {

  // Step 3: People older than 30
  def olderThan30(people: List[Person]): List[Person] =
    people.filter(_.age > 30)

  // Step 4: Group people by their city
  def groupByCity(people: List[Person]): Map[String, List[Person]] =
    people.groupBy(_.city)

  // Step 5: Transform each Person into a descriptive string
  def describe(people: List[Person]): List[String] =
    people.map(p => s"${p.name} is ${p.age} years old and lives in ${p.city}")

  // Step 6: Sort people by age in ascending order
  def sortByAge(people: List[Person]): List[Person] =
    people.sortBy(_.age)

  def main(args: Array[String]): Unit = {

    // Step 2: Create a list of five Person objects
    val people = List(
      Person("Alice", 28, "Chennai"),
      Person("Bob", 35, "Bangalore"),
      Person("Charlie", 24, "Chennai"),
      Person("David", 42, "Hyderabad"),
      Person("Emma", 31, "Bangalore")
    )
    println("List of people:")
    people.foreach(p => println("  " + p))

    // Step 3: Filter people older than 30
    println("\nPeople older than 30:")
    olderThan30(people).foreach(p => println("  " + p))

    // Step 4: Group people by city
    println("\nPeople grouped by city:")
    groupByCity(people).toList.sortBy(_._1).foreach { case (city, group) =>
      println(s"  $city -> ${group.map(_.name).mkString(", ")}")
    }

    // Step 5: Transform each person into a string
    println("\nDescriptions:")
    describe(people).foreach(s => println("  " + s))

    // Step 6: Sort people by age (ascending)
    println("\nPeople sorted by age (ascending):")
    sortByAge(people).foreach(p => println(s"  ${p.name} (${p.age})"))

    // Step 7: Features that case classes provide automatically
    println("\nCase class features:")
    val alice = people.head
    val olderAlice = alice.copy(age = alice.age + 1)
    println("  copy()          : " + olderAlice)
    println("  equality (==)   : " + (alice == Person("Alice", 28, "Chennai")))
    val Person(n, a, c) = alice
    println(s"  pattern match   : name = $n, age = $a, city = $c")
  }
}
