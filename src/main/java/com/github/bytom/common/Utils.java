package com.github.bytom.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
  public static String rfc3339DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final Gson serializer = new GsonBuilder().setDateFormat(rfc3339DateFormat).create();
}
