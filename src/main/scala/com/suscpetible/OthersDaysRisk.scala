package com.suscpetible

import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util

import ch.hsr.geohash.GeoHash
import com.suscpetible.Constants.TIME_BIN

import scala.collection.JavaConverters._
import scala.io.Source

object OthersDaysRisk {
  def main(args: Array[String]): Unit = {
    var baodian = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\baodian.txt")
    var conTime = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\time.csv")
    val others = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\dmubc_user_traje_epidemic_stat_china_i_d.txt")

    val ctTime = conTime.getLines().map(v => {
      val values = v.split(",")
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyy-MM-dd")
      val date = formatter.parse(values(1))

      val bin = date.toInstant.getEpochSecond / 3600 / TIME_BIN
      (values(0), bin)
    }).toMap
    val tmp = baodian.getLines().map(v => {
      val values = v.split("\t")
      val geoHash = GeoHash.geoHashStringWithCharacterPrecision(values(6).toDouble, values(7).toDouble, Constants.GEOHASH_LENGTH)
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
      new ConfirmedCase(list, ctTime(v._1), v._1)
    }).toList.sortBy(v => v.getConfirmedTime)
    var riskMap = new RiskMap(cases.slice(0, 50).asJava)
    val personalRisk = new PersonalRisk()

    val otherCells = others.getLines().map(v => {
      val values = v.split("\t")
      val geoH = GeoHash.fromGeohashString(values(2))
      val geoHash = GeoHash.geoHashStringWithCharacterPrecision(geoH.getBoundingBoxCenterPoint.getLatitude, geoH.getBoundingBoxCenterPoint.getLongitude, Constants.GEOHASH_LENGTH)
      //val time = ZonedDateTime.parse(values(1), DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss").withZone(ZoneId.systemDefault()))
      val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val cnt = values(3).toInt
      val date = formatter.parse(values(1))
      val bin = date.toInstant.getEpochSecond / 3600 / Constants.TIME_BIN
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
      new ConfirmedCase(list, 0, cells._1)
    }).toList
    riskMap = new RiskMap(cases.asJava)

    val writer = new PrintWriter(new File("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\others_days.csv"))
    writer.println("pin,day,risk,near_risk,his_risk,his_near_risk")

    for (i <- otherCases.indices) {
      //val tt = cases.filterNot(v => v.getName.equals(cases(i).getName)).asJava
      val days = otherCases(i).getCells.asScala.groupBy(cell => {
        (cell.getTime * TIME_BIN) / 24
      }).map(cs => {
        val risk = personalRisk.getRisk(cs._2.toList.asJava, riskMap)
        val riskNear = personalRisk.getRiskWithNearest(cs._2.toList.asJava, riskMap)
        val riskHis = personalRisk.getRiskWithHistory(cs._2.toList.asJava, riskMap)
        val riskHisNear = personalRisk.getRiskWithHistAndNearest(cs._2.toList.asJava, riskMap)
        (cs._1, risk, riskNear, riskHis, riskHisNear)
      })
      for (elem <- days) {
        writer.println(s"${otherCases(i).getName}, ${elem._1}, ${elem._2}, ${elem._3}, ${elem._4}, ${elem._5}")
        println(s"${otherCases(i).getName}, ${elem._1}, ${elem._2}, ${elem._3}, ${elem._4}, ${elem._5}")
      }
    }
    writer.close()
    baodian.close()
    conTime.close()
    others.close()
  }
}
