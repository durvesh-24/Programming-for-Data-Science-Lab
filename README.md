# Programming for Data Science Lab (MACSE502) – Scala Programs

**Student:** Durvesh C Mahajan (26MML0055)
**Faculty:** Dr Rajasekhara Babu M, Professor, School of CSE, VIT Vellore
**Semester:** 2026 Fall

## Assessment 9 (Weeks 9 – 12)

| Week | Date | Sl. No. | Title | Program |
|------|------|---------|-------|---------|
| 9  | 10-09-2026 | 18 | Arrays in Scala | [Program_18_Arrays_in_Scala.scala](Assessment-9/Program_18_Arrays_in_Scala.scala) |
| 10 | 17-09-2026 | 19 | Lists, Sets, and Tuples in Scala | [Program_19_Lists_Sets_Tuples_in_Scala.scala](Assessment-9/Program_19_Lists_Sets_Tuples_in_Scala.scala) |
| 11 | 24-09-2026 | 20 | Handling collisions and resolving conflicts in HashMaps | [Program_20_HashMap_Collisions_in_Scala.scala](Assessment-9/Program_20_HashMap_Collisions_in_Scala.scala) |
| 12 | 08-10-2026 | 21 | Exploring advanced functional combinators | [Program_21_Functional_Combinators_in_Scala.scala](Assessment-9/Program_21_Functional_Combinators_in_Scala.scala) |

## Assessment 10 (Weeks 13 – 14)

| Week | Date | Sl. No. | Title | Program |
|------|------|---------|-------|---------|
| 13 | 15-10-2026 | 22 | Case Classes | [Program_22_Case_Classes.scala](Assessment-10/Program_22_Case_Classes.scala) |
| 13 | 15-10-2026 | 23 | Use Saddle to load and manipulate a dataset | [Program_23_Saddle_Vegas_Apple_Prices.scala](Assessment-10/Program_23_Saddle_Vegas_Apple_Prices.scala) |
| 14 | 22-10-2026 | 24 | Use Breeze library | [Program_24_Breeze_Plotly_Fruit_Revenue.scala](Assessment-10/Program_24_Breeze_Plotly_Fruit_Revenue.scala) |
| 14 | 22-10-2026 | 25 | Saddle for data manipulation | [Program_25_Spire_Saddle_Vegas_Price_Analysis.scala](Assessment-10/Program_25_Spire_Saddle_Vegas_Price_Analysis.scala) |

Programs 23–25 use the dataset [`Assessment-10/fruit_prices.csv`](Assessment-10/fruit_prices.csv)
(Date, Fruit, Price, Quantity for 4 fruits over 10 days).

Each folder has an `output/` directory with the console output of every program and the
generated charts (`.html`, open in a browser).

## How to run

The programs use [Scala CLI](https://scala-cli.virtuslab.org/) with Scala 2.12; the
library dependencies are declared at the top of each file (`//> using dep ...`).

```bash
cd Assessment-10
scala-cli run Program_23_Saddle_Vegas_Apple_Prices.scala
```

| Library | Used in | Version |
|---------|---------|---------|
| Saddle (`io.github.pityka::saddle-core`) | 23, 25 | 3.3.0 |
| Breeze (`org.scalanlp::breeze`) | 24 | 2.1.0 |
| Spire (`org.typelevel::spire`) | 25 | 0.17.0 |
| Plotly.Scala (`org.plotly-scala::plotly-render`) | 24 | 0.8.4 |

**Note on Vegas:** Vegas is only published for Scala 2.11 and is no longer maintained, so it
cannot be used together with current Saddle/Spire releases. Vegas works by turning a chart
description into a Vega-Lite specification; programs 23 and 25 build that same Vega-Lite
specification directly and write it to an HTML page that renders the chart.
