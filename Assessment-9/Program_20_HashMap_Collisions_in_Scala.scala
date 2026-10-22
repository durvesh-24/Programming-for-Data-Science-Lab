//> using scala 2.12.15

// Week 11 | Program S. No. 20 | Exercise Program
// Topic : Handling collisions and resolving conflicts in HashMaps
// Problem: Create a HashMap with keys representing student names and values representing
// their grades, then insert multiple entries including a duplicate key with a different
// grade. Write a function to merge two HashMaps, resolving conflicts by taking the higher
// grade. Finally, write a function to handle collisions by chaining, using lists to store
// multiple values for a single key.

import scala.collection.mutable

object HashMapCollisions {

  // Step 3: Merge two HashMaps - when the same student is present in both,
  // keep the higher grade
  def mergeTakeHigher(map1: mutable.HashMap[String, Int],
                      map2: mutable.HashMap[String, Int]): mutable.HashMap[String, Int] = {
    val merged = mutable.HashMap[String, Int]() ++= map1
    for ((name, grade) <- map2) {
      merged.get(name) match {
        case Some(existing) => merged(name) = math.max(existing, grade)
        case None           => merged(name) = grade
      }
    }
    merged
  }

  // Step 4: Separate chaining - each key keeps a List of all the values inserted for it,
  // so a second insert for the same key is chained instead of overwriting
  def insertWithChaining(table: mutable.HashMap[String, List[Int]], key: String, value: Int): Unit = {
    table(key) = table.getOrElse(key, List()) :+ value
  }

  // Step 5: A small hash table with a fixed number of buckets. Keys whose hash codes fall
  // into the same bucket collide, and the collision is resolved by chaining them in a List.
  class ChainedHashTable(numBuckets: Int) {
    private val buckets: Array[List[(String, List[Int])]] = Array.fill(numBuckets)(List())

    def bucketIndex(key: String): Int = math.abs(key.hashCode) % numBuckets

    def put(key: String, value: Int): Unit = {
      val idx = bucketIndex(key)
      val chain = buckets(idx)
      if (chain.exists(_._1 == key)) {
        // Same key: chain the new value with the old values
        buckets(idx) = chain.map { case (k, vs) => if (k == key) (k, vs :+ value) else (k, vs) }
      } else {
        if (chain.nonEmpty)
          println(s"  Collision: '$key' hashed to bucket $idx, already holding ${chain.map(_._1).mkString(", ")}")
        buckets(idx) = chain :+ (key -> List(value))
      }
    }

    def get(key: String): List[Int] =
      buckets(bucketIndex(key)).find(_._1 == key).map(_._2).getOrElse(List())

    def display(): Unit =
      for (i <- buckets.indices) {
        val chain = buckets(i).map { case (k, vs) => s"$k -> ${vs.mkString("[", ", ", "]")}" }
        println(s"  Bucket $i : " + (if (chain.isEmpty) "empty" else chain.mkString("  =>  ")))
      }
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Create a HashMap of student names and grades
    val grades = mutable.HashMap[String, Int]()
    grades("Alice") = 85
    grades("Bob") = 78
    grades("Charlie") = 92
    grades("David") = 70
    println("Initial HashMap          : " + grades.toList.sortBy(_._1))

    // Step 2: Insert a duplicate key with a different grade - the old value is replaced
    println("Grade of Bob before      : " + grades("Bob"))
    grades("Bob") = 88
    println("Grade of Bob after       : " + grades("Bob"))
    println("HashMap after duplicate  : " + grades.toList.sortBy(_._1))
    println("Number of entries        : " + grades.size)

    // Step 3: Merge two HashMaps, resolving conflicts by taking the higher grade
    val section2 = mutable.HashMap("Alice" -> 90, "Charlie" -> 87, "Eva" -> 95, "David" -> 75)
    println("\nSecond HashMap           : " + section2.toList.sortBy(_._1))
    val merged = mergeTakeHigher(grades, section2)
    println("Merged (higher grade)    : " + merged.toList.sortBy(_._1))

    // Step 4: Chaining - store multiple values for a single key using Lists
    val chained = mutable.HashMap[String, List[Int]]()
    insertWithChaining(chained, "Alice", 85)
    insertWithChaining(chained, "Bob", 78)
    insertWithChaining(chained, "Alice", 90)
    insertWithChaining(chained, "Bob", 88)
    insertWithChaining(chained, "Charlie", 92)
    println("\nChained HashMap (key -> list of grades):")
    chained.toList.sortBy(_._1).foreach { case (k, v) => println(s"  $k -> $v") }

    // Step 5: Bucket-level collisions resolved by separate chaining
    println("\nInserting into a hash table with 4 buckets:")
    val table = new ChainedHashTable(4)
    val entries = List("Alice" -> 85, "Bob" -> 78, "Charlie" -> 92, "David" -> 70,
                       "Eva" -> 95, "Frank" -> 66, "Alice" -> 90)
    entries.foreach { case (k, v) => table.put(k, v) }
    println("\nBuckets of the hash table:")
    table.display()
    println("\nLookup Alice   : " + table.get("Alice"))
    println("Lookup Frank   : " + table.get("Frank"))
    println("Lookup Unknown : " + table.get("Unknown"))
  }
}
