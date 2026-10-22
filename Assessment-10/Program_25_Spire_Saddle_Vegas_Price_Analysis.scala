//> using scala 2.12.15
//> using dep io.github.pityka::saddle-core:3.3.0
//> using dep org.typelevel::spire:0.17.0

// Week 14 | Program S. No. 25 | Exercise Program
// Topic : Saddle for data manipulation
// Problem: Analyse fruit prices using Spire for precise statistics and Saddle for data
// manipulation. Compute the mean and variance of prices for each fruit and visualize
// these trends over time using Vegas. This exercise demonstrates the use of Spire for
// numerical precision, advanced data manipulation with Saddle, and effective
// visualization with Vegas.
//
// Note on Vegas: Vegas is only published for Scala 2.11, so the chart is produced as the
// same Vega-Lite specification that Vegas generates and is rendered from an HTML page.
//
// Run: scala-cli run Program_25_Spire_Saddle_Vegas_Price_Analysis.scala
//      (fruit_prices.csv must be in the same folder)

import org.saddle._
import org.saddle.csv.CsvParser
import _root_.spire.math.Rational
import scala.io.Source
import java.io.PrintWriter

object SpireSaddlePriceAnalysis {

  // Step 4: Exact mean of a list of prices using Spire Rational numbers
  def exactMean(xs: Seq[Rational]): Rational = xs.foldLeft(Rational.zero)(_ + _) / Rational(xs.length)

  // Step 4: Exact population variance using Spire Rational numbers
  def exactVariance(xs: Seq[Rational]): Rational = {
    val m = exactMean(xs)
    xs.map(x => (x - m) * (x - m)).foldLeft(Rational.zero)(_ + _) / Rational(xs.length)
  }

  // Step 6: Vega-Lite (Vegas) multi-line chart, one line per fruit
  def vegasMultiLineChart(title: String, rows: Seq[(String, String, Double)], path: String): Unit = {
    val values = rows.map { case (d, f, p) => s"""{"Date": "$d", "Fruit": "$f", "Price": $p}""" }
      .mkString(",\n        ")
    val spec =
      s"""{
         |  "$$schema": "https://vega.github.io/schema/vega-lite/v5.json",
         |  "title": "$title",
         |  "width": 600, "height": 350,
         |  "data": {"values": [
         |        $values
         |  ]},
         |  "mark": {"type": "line", "point": true},
         |  "encoding": {
         |    "x": {"field": "Date", "type": "ordinal", "title": "Date", "axis": {"labelAngle": -45}},
         |    "y": {"field": "Price", "type": "quantitative", "title": "Price (Rs/kg)"},
         |    "color": {"field": "Fruit", "type": "nominal"},
         |    "tooltip": [{"field": "Fruit"}, {"field": "Date", "type": "ordinal"}, {"field": "Price", "type": "quantitative"}]
         |  }
         |}""".stripMargin
    val html =
      s"""<!DOCTYPE html>
         |<html><head><meta charset="utf-8"><title>$title</title>
         |<script src="https://cdn.jsdelivr.net/npm/vega@5.25.0"></script>
         |<script src="https://cdn.jsdelivr.net/npm/vega-lite@5.16.3"></script>
         |<script src="https://cdn.jsdelivr.net/npm/vega-embed@6.22.2"></script>
         |</head><body><div id="vis"></div>
         |<script>vegaEmbed('#vis', $spec);</script>
         |</body></html>""".stripMargin
    val out = new PrintWriter(path)
    try out.write(html) finally out.close()
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Load the dataset into a Saddle Frame
    val path = args.headOption.getOrElse("fruit_prices.csv")
    val text = Source.fromFile(path).mkString.replace("\r", "")
    val frame: Frame[Int, String, String] =
      CsvParser.parseSourceWithHeader[String](Source.fromString(text), recordSeparator = "\n") match {
        case Right(f)  => f
        case Left(err) => sys.error("Could not parse CSV: " + err)
      }
    println(s"Dataset loaded: ${frame.numRows} rows x ${frame.numCols} columns")

    // Step 2: Build a price Series indexed by fruit name (Saddle manipulation)
    val fruitNames: Seq[String] = frame.firstCol("Fruit").toVec.toSeq
    val priceVec: Vec[Double] = frame.firstCol("Price").toVec.map(_.toDouble)
    val priceByFruit: Series[String, Double] = Series(priceVec, Index(fruitNames: _*))

    // Step 3: Group by fruit with Saddle (double-precision statistics)
    val saddleMean: Series[String, Double] = priceByFruit.groupBy.combine(v => v.mean)
    val saddleCount: Series[String, Int] = priceByFruit.groupBy.combine(v => v.length)
    println("\nSaddle group-by mean price per fruit:")
    println(saddleMean.stringify(10))
    println("Records per fruit:")
    println(saddleCount.stringify(10))

    // Step 4: Exact mean and variance per fruit with Spire Rational numbers
    println("Precise statistics per fruit (Spire Rational):")
    println(f"  ${"Fruit"}%-8s ${"Mean"}%12s ${"Variance"}%14s ${"Std Dev"}%10s   Exact mean (fraction)")
    val fruits = fruitNames.distinct.sorted
    val stats = fruits.map { f =>
      val exactPrices = priceByFruit(f).toVec.toSeq.map(p => Rational(BigDecimal(p)))
      (f, exactMean(exactPrices), exactVariance(exactPrices))
    }
    stats.foreach { case (f, m, v) =>
      println(f"  $f%-8s ${m.toDouble}%12.4f ${v.toDouble}%14.4f ${math.sqrt(v.toDouble)}%10.4f   $m")
    }

    // Step 5: Compare naive floating-point sum with Spire's exact arithmetic
    val floatSum = (1 to 10).map(_ => 0.1).sum
    val exactSum = (1 to 10).map(_ => Rational(1, 10)).foldLeft(Rational.zero)(_ + _)
    println(s"\nSum of ten 0.1 values -> Double: $floatSum | Spire Rational: $exactSum")

    // Step 6: Price trend for each fruit over time (Vegas / Vega-Lite)
    val dates: Seq[String] = frame.firstCol("Date").toVec.toSeq
    val chartRows = dates.indices.map(i => (dates(i), fruitNames(i), priceVec.raw(i)))
    vegasMultiLineChart("Fruit Price Trends Over Time", chartRows, "fruit_price_trends.html")
    println("\nChart saved to fruit_price_trends.html")
  }
}
