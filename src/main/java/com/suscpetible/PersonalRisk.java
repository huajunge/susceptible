package com.suscpetible;

import java.util.List;

import static com.suscpetible.Constants.DIS_ERROR;
import static com.suscpetible.Constants.TIME_BIN;

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
            //risk += cell.getProportion() * riskMap.getRiskMap().getOrDefault(cell, 0.0);
            int t = 1;
            long ti = cell.getTime();
            risk += riskMap.getRiskMap().getOrDefault(cell, 0.0);
            for (int i = 0; i <= Constants.MAX_DECAY; i++) {
                for (int j = TIME_BIN; j <= 24; j += TIME_BIN) {
                    double dec = decay(i);
                    risk += dec * riskMap.getRiskMap().getOrDefault(new Cell(cell.getGeohash(), ti - t, 0.0), 0.0);
                    t++;
                }
            }
            risk *= cell.getProportion();
        }
        return risk;
    }

    public double getRiskWithHistory(Cell cell, RiskMap riskMap) {
        int t = 1;
        long ti = cell.getTime();
        double risk = riskMap.getRiskMap().getOrDefault(cell, 0.0);
        for (int i = 0; i <= Constants.MAX_DECAY; i++) {
            for (int j = TIME_BIN; j <= 24; j += TIME_BIN) {
                double dec = decay(i);
                risk += dec * riskMap.getRiskMap().getOrDefault(new Cell(cell.getGeohash(), ti - t, 0.0), 0.0);
                t++;
            }
        }
        risk *= cell.getProportion();
        return risk;
    }

    public double getRiskWithHistory(List<Cell> cells, RiskMap riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            //risk += cell.getProportion() * riskMap.getRiskMap().getOrDefault(cell, 0.0);
            risk += getRiskWithHistory(cell, riskMap);
        }
        return risk;
    }

    public double decay(int days) {
        return Math.pow(10, -days);
        //return 1;
    }

    public double getRiskWithNearest(List<Cell> cells, RiskMap riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            double sum = 0.0;
            sum += riskMap.getRiskMap().getOrDefault(cell, 0.0);
            for (String s : GeoHashUtils.expand(cell.getGeohash(), "")) {
                //sum += DIS_ERROR * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                if (!s.equals(cell.getGeohash())) {
                    double neRisk = getRiskWithHistory(cell, riskMap);
                    sum += DIS_ERROR * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                    //sum += DIS_ERROR * getRiskWithHistory(new Cell(s, cell.getTime(), 0.0), riskMap);
                }
            }
            risk += cell.getProportion() * sum;
        }
        return risk;
    }

    public double getRiskWithHistAndNearest(List<Cell> cells, RiskMap riskMap) {
        double risk = 0;
        for (Cell cell : cells) {
            double sum = 0.0;
            sum += getRiskWithHistory(cell, riskMap);
            for (String s : GeoHashUtils.expand(cell.getGeohash(), "")) {
                //sum += DIS_ERROR * riskMap.getRiskMap().getOrDefault(new Cell(s, cell.getTime(), 0.0), 0.0);
                if (!s.equals(cell.getGeohash())) {
                    sum += DIS_ERROR * getRiskWithHistory(new Cell(s, cell.getTime(), 0.0), riskMap);
                }
            }
            risk += cell.getProportion() * sum;
        }
        return risk;
    }
}
