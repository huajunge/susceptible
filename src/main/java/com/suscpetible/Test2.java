package com.suscpetible;

import scala.Tuple2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : hehuajun3
 * @description :
 * @date : Created in 2020-10-25 14:00
 * @modified by :
 **/
public class Test2 {
    public static void main(String[] args) {
        File file = new File("");
        List<ConfirmedCase> cases = new ArrayList<>();
        List<Tuple2<String, Cell>> cells = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(file)) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
