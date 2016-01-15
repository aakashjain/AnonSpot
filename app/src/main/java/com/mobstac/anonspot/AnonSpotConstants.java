package com.mobstac.anonspot;

import android.text.Html;

import java.net.URL;

/**
 * Created by aakash on 13/1/16.
 */
public class AnonSpotConstants {
    public static final String FIREBASE_URL = "https://anonspot.firebaseio.com";
    public static final String NAME_GEN_URL = "https://passwd.me/api/1.0/get_password.txt?type=pronounceable&separator=hyphen";

    public static final String MALE_SYM = (String) Html.fromHtml("&#x2642;").toString();
    public static final String FEMALE_SYM = (String) Html.fromHtml("&#x2640;").toString();
    public static final String OTHER_SYM = (String) Html.fromHtml("&#x25CB;").toString();

    public static final int USER_HIGHLIGHT = 0xFFE1EA98;
}
