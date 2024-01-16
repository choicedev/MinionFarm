package com.choice.minionfarm.api;

import org.mineacademy.fo.Common;

import java.io.File;

import static com.choice.minionfarm.utils.constants.Constants.FILE_ACTIVE_MINIONS;
import static com.choice.minionfarm.utils.constants.Constants.FILE_MINIONS_CONFIG;

public class FileAPI {

    public static File getFileMinions(){
        try {
            return new File(FILE_MINIONS_CONFIG);
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }

    public static File getFileMinionsFull(){
        try {
            File file = new File(FarmAPI.getInstance().getDataFolder(), FILE_MINIONS_CONFIG);
            if(!file.exists()) {
                file.mkdirs();
            }
            return file;
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }

    public static File getFileActiveMinions(){
        try {
            return new File(FILE_ACTIVE_MINIONS);
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }

    public static File getFileActiveMinionsFull(){
        try {
            File file = new File(FarmAPI.getInstance().getDataFolder()+"/"+FILE_ACTIVE_MINIONS);
            if(!file.exists()) {
                file.mkdirs();
            }
            return file;
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }

}
