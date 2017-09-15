package com.androidchatapp.libs;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Admin on 27-07-2017.
 */

public class Utils {


    public static String defaultDate = "2016-01-01T06:04:57.691Z";
    //application specific
    public static Locale locale = Locale.ENGLISH;
    public final static SimpleDateFormat readFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
    private final static SimpleDateFormat readFormatDB =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
    public final static SimpleDateFormat readFormatDate =
            new SimpleDateFormat("yyyy-MM-dd", locale);
    public final static SimpleDateFormat writeFormatMonth =
            new SimpleDateFormat("MMM yyyy", locale);
    public final static SimpleDateFormat writeFormat =
            new SimpleDateFormat("HH:mm dd MMM yyyy", locale);
    public final static SimpleDateFormat writeFormatTime =
            new SimpleDateFormat("HH:mm", locale);
    public final static SimpleDateFormat writeFormatDateDB = new
            SimpleDateFormat("yyyy-MM-dd", locale);
    private final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", locale);
    /*   public final static SimpleDateFormat writeFormatActivity =
               new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Config.locale);*/
    public final static SimpleDateFormat writeFormatActivityYear =
            new SimpleDateFormat("dd/MM/yyyy", locale);
    private final static SimpleDateFormat queryFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);
    private final static SimpleDateFormat queryFormatDB =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);



}
