package com.suscpetible;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-24 17:20
 * @modified by :
 **/
public class RiskMap {
    private HashMap<Cell, Double> riskMap;

    public RiskMap(List<ConfirmedCase> cases) {
        this.riskMap = new HashMap<>();
        for (ConfirmedCase aCase : cases) {
            for (Map.Entry<Cell, Double> cell : aCase.getRiskMap().entrySet()) {
                if (this.riskMap.containsKey(cell.getKey())) {
                    this.riskMap.put(cell.getKey(), this.riskMap.get(cell.getKey()) + cell.getValue());
                } else {
                    this.riskMap.put(cell.getKey(), cell.getValue());
                }
            }
        }
    }

    public HashMap<Cell, Double> getRiskMap() {
        return riskMap;
    }
}
