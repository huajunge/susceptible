package com.suscpetible

import java.io.File

object RunAll {
  def main(args: Array[String]): Unit = {
    val baseFilePath = s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\"
    val dataSize = 500003
    val baseOutFilePath = s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\${dataSize}_${Constants.DIS_ERROR * 1000}\\"
    val file = new File(baseOutFilePath)
    if (!file.isDirectory) {
      file.mkdir()
    }
    for (geoHashLength <- 6 to 7) {
      for (timeBin <- 2 to(24, 2)) {
        val baseOutFilePath = s"D:\\onedriveEDU\\OneDrive - my.swjtu.edu.cn\\researches\\SuspectedInfectedCrowdsDetection\\data\\${dataSize}_${Constants.DIS_ERROR * 1000}\\${geoHashLength}_${timeBin}_${dataSize}\\"
        val file = new File(baseOutFilePath)
        if (!file.isDirectory) {
          file.mkdir()
        }
        //Risk.main(Array[String](s"$baseOutFilePath\\risk_all.csv", s"$geoHashLength", s"$timeBin"))
        DaysRisk.main(Array[String](s"$baseOutFilePath\\risk_days.csv", s"$geoHashLength", s"$timeBin", s"$baseOutFilePath\\risk_all.csv"))
        //Others.main(Array[String](s"$baseOutFilePath\\others_all_50000.csv", s"$baseFilePath\\dmubc_user_traje_epidemic_stat_china_i_d_$dataSize.txt", s"$geoHashLength", s"$timeBin"))
        OthersDaysRisk.main(Array[String](s"$baseOutFilePath\\others_days_all.csv", s"$baseFilePath\\dmubc_user_traje_epidemic_stat_china_i_d_$dataSize.txt", s"$geoHashLength", s"$timeBin", s"$baseOutFilePath\\others_all.csv"))
      }
    }
  }
}
