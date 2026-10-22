//> using scala 2.12.15
//> using dep org.scalanlp::breeze:2.1.0
//> using dep org.plotly-scala::plotly-render:0.8.4

// Week 14 | Program S. No. 24 | Exercise Program
// Topic : Use Breeze library
// Problem: Use Breeze library to calculate total revenue for each fruit type by
// multiplying price and quantity from a dataset. Then create a bar chart to visualize
// these revenues using Plotly.Scala. This exercise introduces numerical computations
// with Breeze and dynamic visualizations with Plotly.Scala.
//
// Run: scala-cli run Program_24_Breeze_Plotly_Fruit_Revenue.scala
//      (fruit_prices.csv must be in the same folder)

import breeze.linalg.{DenseVector, sum, argmax, argmin}
import plotly._
import plotly.element._
import plotly.layout._
import plotly.Plotly._
import scala.io.Source

object BreezeFruitRevenue {

  def main(args: Array[String]): Unit = {

    // Step 1: Read the dataset (Date, Fruit, Price, Quantity)
    val path = args.headOption.getOrElse("fruit_prices.csv")
    val lines = Source.fromFile(path).getLines().map(_.trim).filter(_.nonEmpty).toList
    val rows = lines.tail.map(_.split(","))
    val fruits: Seq[String] = rows.map(_(1))
    println(s"Records loaded           : ${rows.length}")

    // Step 2: Store Price and Quantity as Breeze DenseVectors
    val price: DenseVector[Double] = DenseVector(rows.map(_(2).toDouble): _*)
    val quantity: DenseVector[Double] = DenseVector(rows.map(_(3).toDouble): _*)
    println("First 5 prices           : " + price(0 until 5))
    println("First 5 quantities       : " + quantity(0 until 5))

    // Step 3: Revenue of every record = price * quantity (element-wise)
    val revenue: DenseVector[Double] = price *:* quantity
    println("First 5 revenues         : " + revenue(0 until 5))

    // Step 4: Total revenue for each fruit type
    val fruitTypes = fruits.distinct.sorted
    val totalRevenue: Seq[(String, Double)] = fruitTypes.map { f =>
      val idx = fruits.indices.filter(i => fruits(i) == f)
      f -> sum(revenue(idx).toDenseVector)
    }
    println("\nTotal revenue per fruit:")
    totalRevenue.foreach { case (f, r) => println(f"  $f%-8s : Rs $r%,.2f") }

    // Step 5: Summary statistics using Breeze
    val revenueByFruit = DenseVector(totalRevenue.map(_._2): _*)
    println(f"\nGrand total revenue      : Rs ${sum(revenue)}%,.2f")
    println(f"Mean revenue per record  : Rs ${breeze.stats.mean(revenue)}%,.2f")
    println(f"Highest earning fruit    : ${totalRevenue(argmax(revenueByFruit))._1}")
    println(f"Lowest earning fruit     : ${totalRevenue(argmin(revenueByFruit))._1}")

    // Step 6: Interactive bar chart using Plotly.Scala
    val bar = Bar(totalRevenue.map(_._1), totalRevenue.map(_._2))
      .withName("Revenue")
      .withMarker(Marker().withColor(Color.RGB(46, 134, 193)))
    val layout = Layout()
      .withTitle("Total Revenue by Fruit Type")
      .withXaxis(Axis().withTitle("Fruit"))
      .withYaxis(Axis().withTitle("Revenue (Rs)"))
    Plotly.plot("fruit_revenue_bar_chart.html", Seq(bar), layout,
                openInBrowser = false, addSuffixIfExists = false)
    println("\nBar chart saved to fruit_revenue_bar_chart.html")
  }
}
