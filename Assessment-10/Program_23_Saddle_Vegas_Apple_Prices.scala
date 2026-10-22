//> using scala 2.12.15
//> using dep io.github.pityka::saddle-core:3.3.0

// Week 13 | Program S. No. 23 | Exercise Program
// Topic : Use Saddle to load and manipulate a dataset
// Problem: Use Saddle to load and manipulate a dataset of fruit prices and quantities,
// filtering for apples and calculating their average price. Then visualize the price
// trend over time using Vegas.
//
// Note on Vegas: Vegas is a Scala wrapper that turns a chart description into a
// Vega-Lite specification and renders it in HTML. It is only published for Scala 2.11, so
// this program builds the same Vega-Lite specification (mark, x/y encodings, title) and
// writes it to an HTML page that renders the chart in the browser.
//
// Run: scala-cli run Program_23_Saddle_Vegas_Apple_Prices.scala
//      (fruit_prices.csv must be in the same folder)

import org.saddle._
import org.saddle.csv.CsvParser
import scala.io.Source
import java.io.PrintWriter

object SaddleApplePrices {

  // Step 7: Vega-Lite (Vegas) line chart written to an HTML page
  def vegasLineChart(title: String, xField: String, yField: String,
                     points: Seq[(String, Double)], path: String): Unit = {
    val values = points.map { case (x, y) => s"""{"$xField": "$x", "$yField": $y}""" }.mkString(",\n        ")
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
         |    "x": {"field": "$xField", "type": "ordinal", "title": "$xField", "axis": {"labelAngle": -45}},
         |    "y": {"field": "$yField", "type": "quantitative", "title": "$yField (Rs/kg)", "scale": {"zero": false}},
         |    "tooltip": [{"field": "$xField", "type": "ordinal"}, {"field": "$yField", "type": "quantitative"}]
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

    // Step 1: Load the CSV dataset into a Saddle Frame
    val path = args.headOption.getOrElse("fruit_prices.csv")
    val text = Source.fromFile(path).mkString.replace("\r", "")
    val frame: Frame[Int, String, String] =
      CsvParser.parseSourceWithHeader[String](Source.fromString(text), recordSeparator = "\n") match {
        case Right(f)  => f
        case Left(err) => sys.error("Could not parse CSV: " + err)
      }
    println(s"Dataset loaded: ${frame.numRows} rows x ${frame.numCols} columns")
    println("Columns: " + frame.colIx.toSeq.mkString(", "))
    println("\nFirst 8 rows of the dataset:")
    println(frame.head(8).stringify(8, 4))

    // Step 2: Convert Price and Quantity columns to numeric Series
    val prices: Series[Int, Double] = frame.firstCol("Price").mapValues(_.toDouble)
    val quantities: Series[Int, Int] = frame.firstCol("Quantity").mapValues(_.toInt)
    println(f"Overall average price    : ${prices.toVec.mean}%.2f")
    println(s"Total quantity (all)     : ${quantities.sum}")

    // Step 3: Filter the rows where Fruit == "Apple"
    val apples: Frame[Int, String, String] = frame.rfilter(row => row.first("Fruit").get == "Apple")
    println(s"\nApple records (${apples.numRows} rows):")
    println(apples.stringify(20, 4))

    // Step 4: Average price of apples
    val applePrices: Series[Int, Double] = apples.firstCol("Price").mapValues(_.toDouble)
    val appleAvg = applePrices.toVec.mean
    println(f"Average price of apples  : Rs $appleAvg%.2f")
    println(f"Minimum apple price      : Rs ${applePrices.toVec.min2}%.2f")
    println(f"Maximum apple price      : Rs ${applePrices.toVec.max2}%.2f")

    // Step 5: Apple price indexed by date
    val dates: Seq[String] = apples.firstCol("Date").toVec.toSeq
    val applePriceByDate: Series[String, Double] = Series(Vec(applePrices.toVec.toSeq: _*), Index(dates: _*))
    println("\nApple price by date:")
    println(applePriceByDate.stringify(20))

    // Step 6: Day-to-day change in apple price
    val change = applePriceByDate.toVec.toSeq.sliding(2).map { case Seq(a, b) => b - a }.toSeq
    println("Day-to-day price change  : " + change.map(c => f"$c%+.2f").mkString(", "))

    // Step 7: Visualise the apple price trend over time (Vegas / Vega-Lite)
    vegasLineChart("Apple Price Trend Over Time", "Date", "Price",
                   dates.zip(applePriceByDate.toVec.toSeq), "apple_price_trend.html")
    println("\nChart saved to apple_price_trend.html")
  }
}
