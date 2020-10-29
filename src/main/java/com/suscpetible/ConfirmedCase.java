package com.suscpetible;

import java.util.HashMap;
import java.util.List;

import static com.suscpetible.Constants.INTERVAL_DAYS;

/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-24 16:15
 * @modified by :
 **/
public class ConfirmedCase {
    private List<Cell> cells;
    private long confirmedTime;
    private HashMap<Cell, Double> riskMap;
    private String name;
    private int timeBin;

    public ConfirmedCase(List<Cell> cells, long confirmedTime, String name, int timeBin) {
        this.cells = cells;
        this.confirmedTime = confirmedTime;
        this.name = name;
        this.timeBin = timeBin;
    }

    public HashMap<Cell, Double> getRiskMap() {
        riskMap = new HashMap<>(cells.size());
        for (Cell cell : cells) {
            long t = cell.getTime();
            int d = (int) ((t - confirmedTime - timeBin * INTERVAL_DAYS) * timeBin / 24.0 + 7);
            if (d < 0) {
                d = 0;
            }
            if (d > 15) {
                d = 15;
            }
            double transm = Constants.transmission[d];
            riskMap.put(cell, transm * cell.getProportion());
        }
        return riskMap;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public long getConfirmedTime() {
        return confirmedTime;
    }

    public String getName() {
        return name;
    }
}
