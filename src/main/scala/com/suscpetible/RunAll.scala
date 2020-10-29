package com.suscpetible

import java.io.File

object RunAll {
  def main(args: Array[String]): Unit = {
    val geoHashLength = 7
    val timeBin = 2
    val dataSize = 50000
    val baseFilePath = s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\"
    val baseOutFilePath = s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\${geoHashLength}_${timeBin}_${dataSize}\\"
    val file = new File(baseOutFilePath)
    if(!file.isDirectory) {
      file.mkdir()
    }
//    if(!file.exists()) {
//      file.dir()
//    }
    Risk.main(Array[String](s"$baseOutFilePath\\risk_all_500000.csv", s"$geoHashLength", s"$timeBin"))
    DaysRisk.main(Array[String](s"$baseOutFilePath\\risk_days_500000.csv", s"$geoHashLength", s"$timeBin"))
    Others.main(Array[String](s"$baseOutFilePath\\others_all_50000.csv", s"$baseFilePath\\dmubc_user_traje_epidemic_stat_china_i_d_$dataSize.txt", s"$geoHashLength", s"$timeBin"))
    OthersDaysRisk.main(Array[String](s"$baseOutFilePath\\others_days_all_50000.csv", s"$baseFilePath\\dmubc_user_traje_epidemic_stat_china_i_d_$dataSize.txt", s"$geoHashLength", s"$timeBin"))
  }
}
