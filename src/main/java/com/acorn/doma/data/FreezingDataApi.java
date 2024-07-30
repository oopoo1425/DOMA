package com.acorn.doma.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FreezingDataApi {

	public static String fetchDataFromApi() throws IOException {
//		일반 인증키
//		(Encoding)	
//		%2Be0w%2FsKfgHqqLeOOGupdVrnGmCfE81u0jINpS51GPDFeppol8RaYe1id0%2BbpEO%2BSq%2BYzOmi%2F%2By77yKtIgPSlqQ%3D%3D
//		일반 인증키
//		(Decoding)	
//		+e0w/sKfgHqqLeOOGupdVrnGmCfE81u0jINpS51GPDFeppol8RaYe1id0+bpEO+Sq+YzOmi/+y77yKtIgPSlqQ==
		
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552061/frequentzoneFreezing/getRestFrequentzoneFreezing"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=%2Be0w%2FsKfgHqqLeOOGupdVrnGmCfE81u0jINpS51GPDFeppol8RaYe1id0%2BbpEO%2BSq%2BYzOmi%2F%2By77yKtIgPSlqQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("searchYearCd","UTF-8") + "=" + URLEncoder.encode("2017", "UTF-8")); /*검색을 원하는 사고년도*/
        urlBuilder.append("&" + URLEncoder.encode("siDo","UTF-8") + "=" + URLEncoder.encode("11", "UTF-8")); /*법정동 시도 코드*/
        urlBuilder.append("&" + URLEncoder.encode("guGun","UTF-8") + "=" + URLEncoder.encode("110", "UTF-8")); /*법정동 시군구 코드*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*결과형식(xml/json)*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*검색건수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        String jsonData = sb.toString();
        
        return jsonData;
    }
	
	private static JsonArray parseJsonData(String jsonData) {
		// JSON 데이터 파싱하여 필요한 정보 추출하는 코드
        JsonElement jsonElement = JsonParser.parseString(jsonData); // JSON 문자열을 JsonElement로 파싱합니다.
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // API 응답의 구조에 맞게 데이터 파싱
        JsonArray items = jsonObject.has("items") && jsonObject.getAsJsonObject("items").has("item")
                ? jsonObject.getAsJsonObject("items").getAsJsonArray("item")
                : new JsonArray(); // 빈 JsonArray 반환

        return items;
    }
	
	public static void main(String[] args) throws IOException {
		//API로부터 데이터 가져오기
		String jsonData = fetchDataFromApi();
		
		//JSON 데이터 파싱하여 필요한 정보 추출
        JsonArray items = parseJsonData(jsonData);
        
        // 추출한 정보를 데이터베이스에 저장
        saveDataToDatabase(items);
        
        System.out.println(items);
    }
	
	private static void saveDataToDatabase(JsonArray items) {
        String url = "jdbc:oracle:thin:@192.168.0.30:1521:xe";
        String user = "seoul_travel";
        String password = "journey";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO D_FREEZING (year, accident, casualties, dead, sertiously, ordinary, report, longitude, latitude, polygon, gname, dname, acc_point) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (JsonElement item : items) {
                    JsonObject touristDataJson = item.getAsJsonObject();
                    
                    stmt.setString(1, getJsonElementAsString(touristDataJson, "year"));
                    stmt.setString(2, getJsonElementAsString(touristDataJson, "accident"));
                    stmt.setString(3, getJsonElementAsString(touristDataJson, "casualties"));
                    stmt.setString(4, getJsonElementAsString(touristDataJson, "dead"));
                    stmt.setString(5, getJsonElementAsString(touristDataJson, "sertiously"));
                    stmt.setString(6, getJsonElementAsString(touristDataJson, "ordinary"));
                    stmt.setString(7, getJsonElementAsString(touristDataJson, "report"));
                    stmt.setString(8, getJsonElementAsString(touristDataJson, "longitude"));
                    stmt.setString(9, getJsonElementAsString(touristDataJson, "latitude"));
                    stmt.setString(10, getJsonElementAsString(touristDataJson, "polygon"));
                    stmt.setString(11, getJsonElementAsString(touristDataJson, "gname"));
                    stmt.setString(12, getJsonElementAsString(touristDataJson, "dname"));
                    stmt.setString(13, getJsonElementAsString(touristDataJson, "acc_point"));
                    
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Error saving data to database: " + e.getMessage());
        }
    }

    private static String getJsonElementAsString(JsonObject jsonObject, String key) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsString() : null;
    }
}
