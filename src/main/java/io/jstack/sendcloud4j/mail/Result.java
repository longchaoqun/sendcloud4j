package io.jstack.sendcloud4j.mail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * API返回结果
 *
 * @author panchenliang
 */
public class Result {
    private static Logger logger = LoggerFactory.getLogger(Result.class);

    public static class CODE {
        public static String ERROR = "error";
        public static String SUCCESS = "success";
    }

    private String message;
    private List<String> errors;

    private String json;

    /**
     * Construct a result from json msg
     *
     * @param jsonStr
     */
    public Result(String jsonStr) {
        this.json = jsonStr;
        JSONObject jsonObj = new JSONObject(jsonStr);
        this.message = jsonObj.getString("message");
        try {
            JSONArray jsonErrors = jsonObj.getJSONArray("errors");
            if (jsonErrors.length() > 0) {
                this.errors = new ArrayList<String>();
            }

            for (int i = 0; i < jsonErrors.length(); i++) {
                this.errors.add(jsonErrors.getString(i));
            }
        } catch (JSONException ex) {
            logger.debug("no error field");
        }

    }

    /**
     * @param message CODE.SUCCESS / CODE.ERROR
     * @param error   ErrorDesc
     */
    public Result(String message, String error) {
        this.message = message;
        if (null != error) {
            this.errors = new ArrayList<String>();
            this.errors.add(error);
        }
    }

    public boolean isSuccess() {
        return (CODE.SUCCESS.equals(message));
    }

    /**
     * Get detail error description from API Response
     *
     * @return error msg would splited by ;
     */
    public String getError() {
        if (null == this.errors) {
            return "";
        }
        if (this.errors.isEmpty()) {
            return "";
        }
        if (1 == this.errors.size()) {
            return this.errors.get(0).toString();
        } else {
            StringBuilder bufErrors = new StringBuilder();
            for (String error : errors) {
                bufErrors.append(error).append(";");
            }
            return bufErrors.toString();
        }
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return json;
    }
}