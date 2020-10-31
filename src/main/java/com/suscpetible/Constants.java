package com.suscpetible;

/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-24 16:41
 * @modified by :
 **/
public class Constants {
    public static double DIS_ERROR = 0.1;
    public static double[] transmission = new double[]{
            0.000683967, //-7
            0.004126712,
            0.01506459,
            0.03788936,
            0.07150035,
            0.1073841,
            0.1339265,
            0.1431614, //0
            0.134376,
            0.1128666,
            0.08611874,
            0.06042452,
            0.03937845,
            0.0240351,
            0.01383596,
            0.007556527
    };

    public static int INTERVAL_DAYS = 5;
    public static int MAX_DECAY = 5;
}
