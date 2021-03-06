package com.suscpetible;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-24 17:32
 * @modified by :
 **/
public class RiskMapWithHistory {
    private HashMap<Cell, Double> riskMapWithHistory;
    private int timeBin;

    public RiskMapWithHistory(List<ConfirmedCase> cases, int timeBin) {
        this.timeBin = timeBin;
        HashMap<Cell, Double> riskMap = new HashMap<>();
        this.riskMapWithHistory = new HashMap<>();
        for (ConfirmedCase aCase : cases) {
            for (Map.Entry<Cell, Double> cell : aCase.getRiskMap().entrySet()) {
                if (riskMap.containsKey(cell.getKey())) {
                    riskMap.put(cell.getKey(), riskMap.get(cell.getKey()) + cell.getValue());
                } else {
                    riskMap.put(cell.getKey(), cell.getValue());
                }
            }
        }
        for (Map.Entry<Cell, Double> cell : riskMap.entrySet()) {
            double risk = cell.getValue();
            int t = 1;
            long ti = cell.getKey().getTime();
            for (int i = 0; i <= Constants.MAX_DECAY; i++) {
                for (int j = timeBin; j <= 24; j += timeBin) {
                    double dec = decay(i);
                    risk += dec * riskMap.getOrDefault(new Cell(cell.getKey().getGeohash(), ti - t, 0.0), 0.0);
                    t++;
                }
            }
            riskMapWithHistory.put(cell.getKey(), risk);
        }
    }

    public HashMap<Cell, Double> getRiskMap() {
        return riskMapWithHistory;
    }

    public double decay(int days) {
        return Math.pow(10, -days);
        //return 1;
    }
}
