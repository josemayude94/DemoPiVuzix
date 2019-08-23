package com.vuzix.sample.barcodefromintent;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Parser {
    String JSONpage = null;

    public Parser(String json)
    {
        JSONpage = json;
    }

    public String parse(){
        JSONParser pars = new JSONParser();
        try{
            JSONObject obj2 = (JSONObject) pars.parse(JSONpage);
            String s2 = obj2.get("Value").toString();
            return s2;
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
            return "WebID invalido.";
        }
        return null;
    }
}
