package com.suscpetible

import java.io.{File, PrintWriter}
import java.util

import scala.io.Source

object Accuracy {
  def main(args: Array[String]): Unit = {
    val suffix = "500003_1.0"
    val filePath = s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\$suffix"
    //val file = Source.fromFile(filePath)
    val precision = 0.001
    val dir = new File(filePath)
    val result = new util.ArrayList[(Int, Int, String, Double, Double, Double, Double)]()
    val writer = new PrintWriter(new File(s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\acc\\acc_$suffix.csv"))
    dir.listFiles().foreach(f => {
      val name = f.getName.split("_")
      for (riskFile <- f.listFiles()) {
        val n = riskFile.getName
        val file = Source.fromFile(riskFile)
        if (n.contains("all")) {
          val count = file.getLines().drop(1).map(v => {
            val value = v.split(",")
            (ifNot(value(1).toDouble, precision), ifNot(value(2).toDouble, precision), ifNot(value(3).toDouble, precision), ifNot(value(4).toDouble, precision))
          }).toList
          val size = count.size
          val acc = count.reduce((v1, v2) => (v1._1 + v2._1, v1._2 + v2._2, v1._3 + v2._3, v1._4 + v2._4))
          //result.add((name(0).toInt, name(1).toInt, n, acc._1 / size.toDouble, acc._2 / size.toDouble, acc._3 / size.toDouble, acc._4 / size.toDouble))
          // writer.println(String.format("%s,%s,%s,%s,%s,%s,%s", name(0).toInt, name(1).toInt, n, acc._1 / size.toDouble, acc._2 / size.toDouble, acc._3 / size.toDouble, acc._4 / size.toDouble))
          writer.println(s"${name(0).toInt},${name(1).toInt},${n},${acc._1 / size.toDouble},${acc._2 / size.toDouble},${acc._3 / size.toDouble},${acc._4 / size.toDouble}")
        }
        file.close()
      }
    })
    writer.close()
  }

  def ifNot(a: Double, b: Double): Int = {
    if (a > b) {
      1
    } else {
      0
    }
  }
}
