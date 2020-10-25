package com.suscpetible;

/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-25 13:42
 * @modified by :
 **/
public class GeoHashUtils {
    private static final int TOP = 0;
    private static final int RIGHT = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;

    private static final int EVEN = 0;
    private static final int ODD = 1;
    private static String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private static String[][] NEIGHBORS;
    private static String[][] BORDERS;

    static {
        NEIGHBORS = new String[2][4];
        BORDERS = new String[2][4];

        BORDERS[ODD][TOP] = "bcfguvyz";
        BORDERS[ODD][RIGHT] = "prxz";
        BORDERS[ODD][BOTTOM] = "0145hjnp";
        BORDERS[ODD][LEFT] = "028b";

        BORDERS[EVEN][TOP] = BORDERS[ODD][RIGHT];
        BORDERS[EVEN][RIGHT] = BORDERS[ODD][TOP];
        BORDERS[EVEN][BOTTOM] = BORDERS[ODD][LEFT];
        BORDERS[EVEN][LEFT] = BORDERS[ODD][BOTTOM];

        NEIGHBORS[ODD][TOP] = "238967debc01fg45kmstqrwxuvhjyznp";
        NEIGHBORS[ODD][RIGHT] = "14365h7k9dcfesgujnmqp0r2twvyx8zb";
        NEIGHBORS[ODD][BOTTOM] = "bc01fg45238967deuvhjyznpkmstqrwx";
        NEIGHBORS[ODD][LEFT] = "p0r21436x8zb9dcf5h7kjnmqesgutwvy";

        NEIGHBORS[EVEN][TOP] = NEIGHBORS[ODD][RIGHT];
        NEIGHBORS[EVEN][RIGHT] = NEIGHBORS[ODD][TOP];
        NEIGHBORS[EVEN][BOTTOM] = NEIGHBORS[ODD][LEFT];
        NEIGHBORS[EVEN][LEFT] = NEIGHBORS[ODD][BOTTOM];
    }

    /**
     * 求与当前geohash相邻的8个格子的geohash值。
     *
     * @param geohash
     * @param suffix  数据库查询中前缀匹配使用的通配符
     * @return string数组，周围格子的geohash值
     */
    public static String[] expand(String geohash, String suffix) {

        String left = calculate(geohash, LEFT);
        String right = calculate(geohash, RIGHT);
        String top = calculate(geohash, TOP);
        String bottom = calculate(geohash, BOTTOM);

        String topLeft = calculate(top, LEFT);
        String topRight = calculate(top, RIGHT);
        String bottomLeft = calculate(bottom, LEFT);
        String bottomRight = calculate(bottom, RIGHT);

        return new String[]{topLeft + suffix, top + suffix, topRight + suffix, left + suffix, geohash + suffix, right + suffix, bottomLeft + suffix, bottom + suffix, bottomRight + suffix};
    }

    /**
     * 递归计算当前区域特定方向的geohash值
     *
     * @param geohash
     * @param direction 偏移方向
     * @return 周围区域的geohash值，超出边界则返回空字符串""
     */
    private static String calculate(String geohash, int direction) {
        if ("".equals(geohash))      //如果递归到第一个字符仍然处于边界，则不存在这一方向的相邻格子
        {
            return "";
        }
        int length = geohash.length();
        char lastChar = geohash.charAt(length - 1);
        int charType = (geohash.length() % 2) == 1 ? ODD : EVEN;  //最后一位是奇数还是偶数
        String base = geohash.substring(0, length - 1);
        if (BORDERS[charType][direction].indexOf(lastChar) != -1) { //判断对后一位是否处在边界
            base = calculate(base, direction);
        }
        if (BORDERS[charType][direction].indexOf(lastChar) != -1 && (geohash.length() == 1 ||
                (geohash.length() != 1) && ("".equals(base)))) {
            return "";
        } else {
            return base + NEIGHBORS[charType][direction].charAt(BASE32.indexOf(lastChar));
        }
//        if (!"".equals(base)) {
//            return base + NEIGHBORS[charType][direction].charAt(BASE32.indexOf(lastChar));
//        } else {
//            return "";
//        }
    }
}
