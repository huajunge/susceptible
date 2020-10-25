package com.suscpetible;

import java.util.List;

import static com.suscpetible.Constants.DIS_ERROR;

/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-24 17:52
 * @modified by :
 **/
public class PersonalRisk {
    public double getRisk(List<Cell> cells, RiskMap riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            risk += cell.getProportion() * riskMap.getRiskMap().getOrDefault(cell, 0.0);
        }
        return risk;
    }

    public double getRiskWithHistory(List<Cell> cells, RiskMapWithHistory riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            risk += cell.getProportion() * riskMap.getRiskMap().getOrDefault(cell, 0.0);
        }
        return risk;
    }

    public double getRiskWithNearest(List<Cell> cells, RiskMap riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            double sum = 0.0;
            sum += riskMap.getRiskMap().getOrDefault(cell, 0.0);
            for (String s : GeoHashUtils.expand(cell.getGeohash(), "")) {
                //sum += DIS_ERROR * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                if (!s.equals(cell.getGeohash())) {
                    sum += 0.125 * DIS_ERROR * cell.getProportion() * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                }
            }
            risk += cell.getProportion() * sum;
        }
        return risk;
    }

    public double getRiskWithNearest(List<Cell> cells, RiskMapWithHistory riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            double sum = 0.0;
            sum += riskMap.getRiskMap().getOrDefault(cell, 0.0);
            for (String s : GeoHashUtils.expand(cell.getGeohash(), "")) {
                //sum += DIS_ERROR * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                if (!s.equals(cell.getGeohash())) {
                    sum += 0.125 * DIS_ERROR * cell.getProportion() * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                }
            }
            risk += cell.getProportion() * sum;
        }
        return risk;
    }
}
