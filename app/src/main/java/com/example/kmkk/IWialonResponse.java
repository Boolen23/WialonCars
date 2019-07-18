package com.example.kmkk;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public interface IWialonResponse {
   void WialonResponse(String param) throws ParseException, JSONException;
}

