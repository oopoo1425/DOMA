package com.acorn.doma.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.acorn.doma.cmn.PLog;
import com.acorn.doma.domain.Accident;
import com.acorn.doma.mapper.AccMapper;
@Service
public class AccInfoServiceImpl implements AccInfoService, PLog{
	private final String serviceKey;
	private final AccMapper accMapper;
	 @Autowired
	    public AccInfoServiceImpl(AccMapper accMapper, @Qualifier("accInfoServiceKey") String serviceKey) {
	        this.accMapper = accMapper;
	        this.serviceKey = serviceKey;
	}
	@Override
	public String fetchDataFromApi() throws IOException {
		String xmlData = "";
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
		urlBuilder.append("/" +  URLEncoder.encode(serviceKey,"UTF-8") ); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
		urlBuilder.append("/" +  URLEncoder.encode("xml","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
		urlBuilder.append("/" + URLEncoder.encode("AccInfo","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
		urlBuilder.append("/" + URLEncoder.encode("1","UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
		urlBuilder.append("/" + URLEncoder.encode("500","UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/xml");
		System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
		BufferedReader rd;

		// 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
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
		
		return sb.toString();
	}
	@Override
	public List<Accident> parseXmlData(String xmlData) {
        List<Accident> accInfoList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new java.io.ByteArrayInputStream(xmlData.getBytes("UTF-8")));

            // Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("row");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;

                    String accId = eElement.getElementsByTagName("acc_id").item(0).getTextContent();
                    String occrDate = eElement.getElementsByTagName("occr_date").item(0).getTextContent();
                    String occrTime = eElement.getElementsByTagName("occr_time").item(0).getTextContent();
                    String endDate=eElement.getElementsByTagName("exp_clr_date").item(0).getTextContent();
                    String endTime=eElement.getElementsByTagName("exp_clr_time").item(0).getTextContent();
                    String accType= eElement.getElementsByTagName("acc_type").item(0).getTextContent();
                    String accDtype=eElement.getElementsByTagName("acc_dtype").item(0).getTextContent();
                    String info = eElement.getElementsByTagName("acc_info").item(0).getTextContent();                   
                    double longitude = 0.0;
                    double latitude = 0.0;

                    try {
                        longitude = Double.parseDouble(eElement.getElementsByTagName("grs80tm_x").item(0).getTextContent());
                        latitude = Double.parseDouble(eElement.getElementsByTagName("grs80tm_y").item(0).getTextContent());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        // Handle the case where conversion fails, for example:
                        // - Log the error
                        // - Skip this record
                        // - Set longitude and latitude to default values
                    }

                    accInfoList.add(new Accident(accId, occrDate, occrTime, endDate, endTime, accType, accDtype, info, longitude, latitude));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accInfoList;
    }
	
	public void insertAccidentData() {
        try {
            String xmlData = fetchDataFromApi();
            List<Accident> accInfoList = parseXmlData(xmlData);
            accMapper.doDeleteAll();
            for (Accident accident : accInfoList) {
                accMapper.dataInsert(accident);
            }
        } catch (IOException e) {
            // handle exceptions related to fetching data
            e.printStackTrace();
        } catch (Exception e) {
            // handle other exceptions
            e.printStackTrace();
        }
    }
}