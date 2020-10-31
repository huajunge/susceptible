package com.suscpetible

import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util

import ch.hsr.geohash.GeoHash

import scala.collection.JavaConverters._
import scala.io.Source

object Others {
  def main(args: Array[String]): Unit = {
    val baodian = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\baodian.txt")
    val conTime = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\time.csv")
    val others = Source.fromFile(args(1))
    val geoHashlength = args(2).toInt
    val timeBIN = args(3).toInt
    val ctTime = conTime.getLines().map(v => {
      val values = v.split(",")
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyy-MM-dd")
      val date = formatter.parse(values(1))

      val bin = date.toInstant.getEpochSecond / 3600 / timeBIN
      (values(0), bin)
    }).toMap

    val tmp = baodian.getLines().map(v => {
      val values = v.split("\t")
      val geoHash = GeoHash.geoHashStringWithCharacterPrecision(values(6).toDouble, values(7).toDouble, geoHashlength)
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss")
      val date = formatter.parse(values(1))
      val bin = date.toInstant.getEpochSecond / 3600 / timeBIN
      (values(0), bin, geoHash)
    }).toList.groupBy(v => (v._1, v._2)).map(v => {
      val all = v._2.size
      val time = v._1._2
      val cell = v._2.groupBy(v => v._3).map(v => {
        new Cell(v._1, time, v._2.size.toDouble / all.toDouble)
      })
      //println((v._1._1, cell))
      (v._1._1, cell, v._2)
    })
    val cases = tmp.groupBy(v => v._1).map(v => {
      val list = new util.ArrayList[Cell]()
      for (elem <- v._2) {
        list.addAll(elem._2.toList.asJava)
      }
      new ConfirmedCase(list, ctTime(v._1), v._1, timeBIN)
    }).toList.sortBy(v => v.getConfirmedTime)
    var riskMap = new RiskMap(cases.slice(0, 50).asJava)

    val otherCells = others.getLines().map(v => {
      val values = v.split("\t")
      val geoH = GeoHash.fromGeohashString(values(2))
      val geoHash = GeoHash.geoHashStringWithCharacterPrecision(geoH.getBoundingBoxCenterPoint.getLatitude, geoH.getBoundingBoxCenterPoint.getLongitude, geoHashlength)
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val cnt = values(3).toInt
      val date = formatter.parse(values(1))
      val bin = date.toInstant.getEpochSecond / 3600 / timeBIN
      (values(0), bin, geoHash, cnt)
    }).toList.groupBy(v => (v._1, v._2)).map(timeBin => {
      var all = 0
      for (elem <- timeBin._2) {
        all += elem._4
      }
      //val all = v._2.reduce((v1, v2) => v1._4 + v2._4)
      val time = timeBin._1._2
      val cell = timeBin._2.groupBy(v => v._3).map(v => {
        var cnt = 0
        for (elem <- v._2) {
          cnt += elem._4
        }
        new Cell(v._1, time, cnt / all.toDouble)
      })
      //println((v._1._1, cell))
      (timeBin._1._1, cell, timeBin._2)
    })

    val otherCases = otherCells.groupBy(v => v._1).map(cells => {
      val list = new util.ArrayList[Cell]()
      for (elem <- cells._2) {
        list.addAll(elem._2.toList.asJava)
      }
      new ConfirmedCase(list, 0, cells._1, timeBIN)
    }).toList

    val personalRisk = new PersonalRisk(timeBIN)

    riskMap = new RiskMap(cases.asJava)
    val writer = new PrintWriter(new File(args(0)))
    writer.println("pin,risk,near_risk,his_risk,his_near_risk")

    for (i <- otherCases.indices) {
      val risk = personalRisk.getRisk(otherCases(i).getCells, riskMap)
      val riskNear = personalRisk.getRiskWithNearest(otherCases(i).getCells, riskMap)
      val riskHis = personalRisk.getRiskWithHistory(otherCases(i).getCells, riskMap)
      val riskHisNear = personalRisk.getRiskWithHistAndNearest(otherCases(i).getCells, riskMap)
      writer.println(s"${otherCases(i).getName},$risk,$riskNear,$riskHis,$riskHisNear")
      if (i % 1000 == 0) {
        println(s"-----$i------")
      }
    }
    baodian.close()
    conTime.close()
    others.close()
    writer.close()
  }
}
