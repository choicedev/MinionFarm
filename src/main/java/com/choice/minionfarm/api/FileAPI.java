package com.choice.minionfarm.api;

import com.choice.minionfarm.Main;
import com.choice.minionfarm.settings.messages.MessageRepository;
import org.mineacademy.fo.Common;

import java.io.File;

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


    public static MessageRepository getMessagesRepository(){
        try {
            return Main.messageRepository;
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }


}
