package com.example.administrator.discussapplication;

/**
 * Created by Administrator on 2/22/2015.
 */

import java.util.ArrayList;

public class GridViewConfig {
    private static ArrayList<String> resim_list=new ArrayList<String>();

    public static ArrayList<String> getResim_list() {
        return resim_list;

    }

    public static void setResim_list(ArrayList<String> resim_list) {
        GridViewConfig.resim_list = resim_list;
    }
    public static void addImageUrls(){
        //  Here you have to specify your image url path
        resim_list.add("http://www......../image1.png");

        resim_list.add("http://www......../image2.png");

        resim_list.add("http://www......../image3.png");

        resim_list.add("http://www......../image4.png");

        resim_list.add("http://www......../image5.png");
        resim_list.add("http://www......../image6.png");


    }
}
