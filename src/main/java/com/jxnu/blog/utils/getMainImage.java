package com.jxnu.blog.utils;

public class getMainImage {
    public static String getMainImage(String code){
        String mainImage=null;
        for(int i=0;i<code.length()-66;i++){
            if(code.substring(i,i+4).equals("src=")){
                i=i+4;
                mainImage= code.substring(i,i+67);
                break;
            }
        }
        return mainImage;
    }
}
