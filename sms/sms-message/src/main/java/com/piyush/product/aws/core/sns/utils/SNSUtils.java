/*
 * 
 */
package com.piyush.product.aws.core.sns.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// TODO: Auto-generated Javadoc
/**
 * The Class SNSUtils.
 */
public class SNSUtils {

	/**
	 * Checks if is empty string.
	 *
	 * @param string
	 *            the string
	 * @return true, if is empty string
	 */
	public static boolean isEmptyString(String string) {
		if (string == null) {
			return true;
		} else if (string.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is empty list.
	 *
	 * @param list
	 *            the list
	 * @return true, if is empty list
	 */
	public static boolean isEmptyList(List list) {
		if (list == null) {
			return true;
		} else if (list.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is null object.
	 *
	 * @param o
	 *            the o
	 * @return true, if is null object
	 */
	public static boolean isNullObject(Object o) {

		if (o == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * JSON array to json array.
	 *
	 * @param arrobj
	 *            the arrobj
	 * @return the json array
	 * @throws Exception
	 *             the exception
	 */
	public JsonArray JSONArrayToJsonArray(JSONArray arrobj) throws Exception {
		JsonArray jar = new JsonArray();
		for (int i = 0; i < arrobj.length(); i++) {
			JSONObject jsonObj = arrobj.getJSONObject(i);
			JsonParser jsonParser = new JsonParser();
			JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObj.toString());
			jar.add(gsonObject);
		}
		return jar;
	}

}
