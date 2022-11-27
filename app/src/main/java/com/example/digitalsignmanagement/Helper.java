package com.example.digitalsignmanagement;

import android.content.Context;
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
    private static final String TAG = "Helper";

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }

    public static String setConfigValue(String url) throws ConfigurationException {

        Configurations configs = new Configurations();

        // obtain the configuration
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder("config.properties");
        PropertiesConfiguration config = builder.getConfiguration();

        // update property
        config.addProperty("url", url);

        // save configuration
        builder.save();



        return null;
    }



    }