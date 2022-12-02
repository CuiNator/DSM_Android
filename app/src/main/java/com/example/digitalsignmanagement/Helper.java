package com.example.digitalsignmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Helper {

    private static final String url = null;

    public static SharedPreferences getPrefs(Context context){
        return  context.getSharedPreferences(url,Context.MODE_PRIVATE);
    }

    public static void insertData(Context context,String key,String value){
        SharedPreferences.Editor editor=getPrefs(context).edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String retriveData(Context context,String key){
        return getPrefs(context).getString(key,"no_data_found");
    }
}