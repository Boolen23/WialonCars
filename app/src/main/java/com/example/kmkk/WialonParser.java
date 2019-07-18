package com.example.kmkk;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class WialonParser extends TimerTask {
    public  WialonParser(MainActivity activity, String NumberOfCar){
        this.Reciver = activity;
        this.Activity = activity;
        this.NumberOfCar = NumberOfCar;
        LoadParametrs();
    }
    private MainActivity Activity;
    private IDataReciver Reciver;
    private String NumberOfCar;
    private String SessionId;
    private String ObjectName;
    private long ObjectId;
    private WialonConnector Connector;

    protected void Recive() {
        ReciveSensors();
    }
    private void LoadParametrs(){
        ObjectId = 0;
        SessionId = null;
        ObjectName = null;
        Registration();
        ReciveWialonObject();
    }

    private void Registration() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("token", "598e3dbbd37812b96cf87d2f25d95816FFA15E1C5B0DEFE4BABAD22D5D2C22E8AC94A1FA");
        resultJson.put("operateAs", "geolead123");
        Connector = new WialonConnector();
        try {
            String resultJSon = Connector.execute("https://hst-api.wialon.com/wialon/ajax.html?svc=token/login&params=" + resultJson.toString()).get();
            ParseSession(resultJSon);
        }
        catch (ExecutionException e) {  SetParseResult("Method Registration"+"\n"+e.toString()); }
        catch (InterruptedException e) {   SetParseResult("Method Registration"+"\n"+e.toString());  }
        catch (Exception e){SetParseResult("Method Registration"+"\n"+e.toString()); }

    }
    private void ReciveWialonObject(){
        JSONObject search = new JSONObject();
        search.put("itemsType", "avl_unit");
        search.put("propName", "sys_name");
        search.put("propValueMask", NumberOfCar);
        search.put("sortType", "sys_name");

        JSONObject resultJson = new JSONObject();
        resultJson.put("spec", search);
        resultJson.put("force", 1);
        resultJson.put("flags", 1);
        resultJson.put("from", 0);
        resultJson.put("to", 0);
        Connector = new WialonConnector();
        try {
            String resultJSon = Connector.execute("https://hst-api.wialon.com/wialon/ajax.html?svc=core/search_items&params=" + resultJson + "&sid=" + SessionId).get();
            ParseObjectId(resultJSon);
        }
        catch (ExecutionException e) { SetParseResult("Method ReciveObject"+"\n"+e.toString()); }
        catch (InterruptedException e) { SetParseResult("Method ReciveObject"+"\n"+e.toString());}
        catch (Exception e) { SetParseResult("Method ReciveObject"+"\n"+e.toString()); }
    }
    private void ReciveSensors(){
        JSONObject resultJson = new JSONObject();
        resultJson.put("itemId", ObjectId);
        resultJson.put("lastTime", 0);
        resultJson.put("lastCount", 1);
        resultJson.put("flags", 1);
        resultJson.put("flagsMask", 65281);
        resultJson.put("loadCount", 1);
        Connector = new WialonConnector();
        try {
            String resultJSon = Connector.execute("https://hst-api.wialon.com/wialon/ajax.html?svc=messages/load_last&params=" + resultJson + "&sid=" + SessionId).get();
            ParseSensors(resultJSon);
        }
        catch (ExecutionException e) { SetParseResult("Method ReciveSensors"+"\n"+e.toString()); }
        catch (InterruptedException e) { SetParseResult("Method ReciveSensors"+"\n"+e.toString());}
        catch (Exception e) { SetParseResult("Method ReciveSensors"+"\n"+e.toString()); }
    }

    private void ParseSession(String response){
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONObject jsonObj = (JSONObject) obj;
            SessionId = jsonObj.get("eid").toString();
        } catch (ParseException e) {
            SetParseResult("Method ParseSession"+"\n"+e.toString());
        } catch (Exception e) {
            SetParseResult("Method ParseSession"+"\n"+e.toString());
        }
    }
    private void ParseObjectId(String response){
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray items = (JSONArray)jsonObj.get("items");
            JSONObject WialonUnit = (JSONObject)items.get(0);
            ObjectName = WialonUnit.get("nm").toString();
            ObjectId = Long.parseLong(WialonUnit.get("id").toString());
        } catch (ParseException e) {
            SetParseResult("Method ParseObject"+"\n"+e.toString());
        }
        catch (Exception e) {
            SetParseResult("Method ParseObject"+"\n"+e.toString());
        }
    }
    private void ParseSensors(String response){
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray result = (JSONArray)jsonObj.get("messages");
            JSONObject SensorList =(JSONObject)result.get(0);
            long time = Long.parseLong(SensorList.get("t").toString());
            JSONObject Sensors = (JSONObject)SensorList.get("p");
            String W0 = Sensors.get("temp_1wire_0").toString();
            String W1 = Sensors.get("temp_1wire_1").toString();
            String Pin = Sensors.get("pin").toString();
            int pinInt = Integer.parseInt(Pin);
            String PinResult = pinInt == 0 ? "Дверь открыта":"Дверь закрыта";
            SetParseResult(ObjectName+"\n\n"+PinResult+"\n"+"Температура: "+ W0+"\n"+"Темп. дверь: "+W1+ "\n\n"+formatDate(time));
        }
        catch (ParseException e) { SetParseResult("Method ParseSensors"+"\n"+e.toString());}
        catch (Exception e) {  SetParseResult("Method ParseSensors"+"\n"+e.toString());}
    }
    private String formatDate(long milliseconds) {
        milliseconds*=1000;
        DateFormat sdf = new SimpleDateFormat("dd.MM.yy' 'HH:mm:ss");
        Date date = new Date(milliseconds);
        sdf.format(date);
        return "На " + sdf.format(date);
    }
    public void SetParseResult (String param){
        Reciver.NewDataRecived(param);
    }

    @Override
    public void run() {
        Activity.runOnUiThread(new Runnable() {
            @Override
            public void run() { Recive();}
        });

    }
}
