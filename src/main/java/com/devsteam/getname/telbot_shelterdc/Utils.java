package com.devsteam.getname.telbot_shelterdc;

import com.devsteam.getname.telbot_shelterdc.model.Color;

public class Utils {

    public static boolean stringValidation(String str){
        if(str!=null&&!str.isEmpty()&&!str.isBlank()){
            return true;
    }return false;
}
}
