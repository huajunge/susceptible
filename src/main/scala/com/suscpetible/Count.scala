package com.suscpetible

import scala.io.Source

object Count {
  def main(args: Array[String]): Unit = {
    val others = Source.fromFile("D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\dmubc_user_traje_epidemic_stat_china_i_d.txt")
    val result = others.getLines().map(v => {
      val values = v.split("\t")
      values(0)
    }).toList.distinct
    println(result.size)
  }
}
