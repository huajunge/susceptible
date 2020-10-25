package com.suscpetible

import java.text.SimpleDateFormat
import java.util

import ch.hsr.geohash.GeoHash

import scala.collection.JavaConverters._
import scala.io.Source

object Test {
  def main(args: Array[String]): Unit = {
    var baodian = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\baodian.txt")
    var conTime = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\time.csv")
    val ctTime = conTime.getLines().map(v => {
      val values = v.split(",")
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyy-MM-dd")
      val date = formatter.parse(values(1))

      val bin = date.toInstant.getEpochSecond / 3600 / Constants.TIME_BIN
      (values(0), bin)
    }).toMap
    val tmp = baodian.getLines().map(v => {
      val values = v.split("\t")
      val geoHash = GeoHash.geoHashStringWithCharacterPrecision(values(6).toDouble, values(7).toDouble, 7)
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss")
      val date = formatter.parse(values(1))
      val bin = date.toInstant.getEpochSecond / 3600 / Constants.TIME_BIN
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
      new ConfirmedCase(list, ctTime(v._1))
    }).toList
    var riskMap = new RiskMap(cases.asJava)
    var his = new RiskMapWithHistory(cases.asJava)
    val gt =new GeoHashUT()
//    for (elem <- riskMap.getRiskMap.entrySet().asScala) {
//      val geo = gt.decode(elem.getKey.getGeohash)
//      println(s"${geo(0)}")
//      println(elem.getKey + "," + elem.getValue)
//    }
//    println("--------")
//    println("--------")
//    for (elem <- his.getRiskMap.entrySet().asScala) {
//      println(elem.getKey + "," + elem.getValue)
//    }

    val personalRisk = new PersonalRisk()
    //    println(personalRisk.getRisk(cases(10).getCells, riskMap))
    //    println(personalRisk.getRiskWithNearest(cases(10).getCells, riskMap))
    //    println(personalRisk.getRiskWithHistory(cases(10).getCells, his))
    //    println(personalRisk.getRiskWithNearest(cases(10).getCells, his))

    for (i <- 0 to 59) {
      println(s"-----$i--------")
      println(personalRisk.getRisk(cases(i).getCells, riskMap))
      println(personalRisk.getRiskWithNearest(cases(i).getCells, riskMap))
      println(personalRisk.getRiskWithHistory(cases(i).getCells, his))
      println(personalRisk.getRiskWithNearest(cases(i).getCells, his))
    }
  }
}
