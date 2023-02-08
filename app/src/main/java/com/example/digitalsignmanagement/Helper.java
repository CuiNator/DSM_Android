package com.example.digitalsignmanagement;

import android.content.Context;
import android.content.SharedPreferences;

//Class to save and retrieve data between activities
public final class Helper {

    private static final String url = null;

    public static SharedPreferences getPrefs(Context context){
        return  context.getSharedPreferences(url,Context.MODE_PRIVATE);
    }

    public static void insertConnectionData(Context context, String key, String value){
        SharedPreferences.Editor editor=getPrefs(context).edit();
        editor.putString(key,value);
        editor.commit();}

    public static String retrieveConnectionData(Context context, String key){

        String data = getPrefs(context).getString(key,"no_data_found");
        if (data == "no_data_found"){
            data = "http://10.0.2.2:8080";
            return data;
        }
        return data;}

    public static void insertUserData(Context context,String name,String token, String id){
        SharedPreferences.Editor editor=getPrefs(context).edit();
        editor.putString("userName",name);
        editor.putString("token",token);
        editor.putString("userId",id);
        editor.commit();}

    public static void insertUserId(Context context, String id){
        SharedPreferences.Editor editor=getPrefs(context).edit();
        editor.putString("userId",id);
        editor.commit();}

    public static String retrieveUserName(Context context){
        String data = getPrefs(context).getString("userName","no_data_found");
        return data;}

    public static String retrieveToken(Context context){
        String data = getPrefs(context).getString("token","no_data_found");
        return data;}

    public static String retrieveUserId(Context context){
        String data = getPrefs(context).getString("userId","no_data_found");
        return data;}

    public static void insertDocData(Context context,String docName, String docId) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("docName", docName);
        editor.putString("docId", docId);
        editor.commit();}

    public static String retrieveDocId(Context context){
        String data = getPrefs(context).getString("docId","no_data_found");
        return data;}

    public static String retrieveDocName(Context context){
        String data = getPrefs(context).getString("docName","no_data_found");
        return data;}

    public static void insertBitmap(Context context, String bytearray) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("bitmap", bytearray);
        editor.commit();}

    public static String retrieveBitmap(Context context){
        String data = getPrefs(context).getString("bitmap","no_data_found");
        return data;}

    public static void insertLastUser(Context context, String lastUser) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("lastUser", lastUser);
        editor.commit();}

    public static String retrieveLastUser(Context context){
        String data = getPrefs(context).getString("lastUser","E-Mail");
        return data;}
}