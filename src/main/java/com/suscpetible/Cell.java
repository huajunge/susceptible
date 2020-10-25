package com.suscpetible;

import java.util.Objects;

/**
 * @author : hehuajun3
 * @description : The basic resolution
 * @date : Created in 2020-10-24 16:06
 * @modified by :
 **/
public class Cell {
    private String geohash;
    private long time;
    private double proportion;

    public Cell(String geohash, long time, double proportion) {
        this.geohash = geohash;
        this.time = time;
        this.proportion = proportion;
    }

    public String getGeohash() {
        return geohash;
    }

    public long getTime() {
        return time;
    }

    public double getProportion() {
        return proportion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cell cell = (Cell) o;
        return time == cell.time &&
                Objects.equals(geohash, cell.geohash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geohash, time);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "geohash='" + geohash + '\'' +
                ", time=" + time +
                ", proportion=" + proportion +
                '}';
    }
}
