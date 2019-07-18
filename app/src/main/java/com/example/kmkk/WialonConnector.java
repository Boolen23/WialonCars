package com.example.kmkk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WialonConnector extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String ... params) {
        try { return getText(params[0]);  }
            catch (Exception e) {  return  e.toString() ; }

    }
    @Override
    protected  void  onPreExecute(){

    }
    protected void onPostExecute(String res){
        super.onPostExecute(res);

    }

    private String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }
}
