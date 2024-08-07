package com.acorn.doma.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acorn.doma.cmn.PLog;
import com.acorn.doma.domain.Accident;
import com.acorn.doma.domain.Point;
import com.acorn.doma.service.AccInfoService;
import com.acorn.doma.service.FreezingService;
import com.acorn.doma.service.PointService;

@Controller
@RequestMapping("main")
public class MainController implements PLog {

	@Autowired
	AccInfoService accInfoService;
	
	@Autowired
	FreezingService freezingService;
	
	@Autowired
	PointService pointService;
	
	public MainController() {
		log.debug("┌──────────────────────────────┐");
		log.debug("│ MainController()             │");
		log.debug("└──────────────────────────────┘");
	}
	
	//http://localhost:8080/doma/main/main.do
	@RequestMapping(value="/emergency.do"
					,method=RequestMethod.GET
					,produces = "text/plain;charset=UTF-8")
	public String emergency(Model model) throws SQLException {
		log.debug("┌──────────────────────────────┐");
		log.debug("│ main()                       │");
		log.debug("└──────────────────────────────┘");
		
		String viewName = "main/main_emergency_info";

		List<Accident> accList = accInfoService.fullTableScan();
		
		model.addAttribute("accList", accList);
		
		List<Accident> dolbalList = accInfoService.dolbalRetrieve();
		model.addAttribute("dolbalList", dolbalList);
		List<Accident> etcList = accInfoService.etcRetrieve();
		model.addAttribute("etcList", etcList);
		return viewName;
	}
	
	@RequestMapping(value="/freezing.do"
					,method=RequestMethod.GET
					,produces = "text/plain;charset=UTF-8")
	public @ResponseBody String freezing(Model model, @RequestParam(value = "years", required = false) List<Integer> years) throws SQLException {
		log.debug("┌──────────────────────────────┐");
		log.debug("│ freezingMain()               │");
		log.debug("└──────────────────────────────┘");
		
		String viewName = "main/main_freezing_info";
		if (years == null || years.isEmpty()) {
		    years = Arrays.asList(2018, 2019, 2020, 2021, 2022, 2023);
		}
		
	 // Freezing 데이터 조회
	    List<Map<String, Object>> freezingData;
	    try {
	        freezingData = freezingService.selectFreezingData(years);
	    } catch (IOException e) {
	        log.error("Error retrieving freezing data", e);
	        throw new SQLException("Unable to retrieve freezing data", e);
	    }
	    model.addAttribute("freezingData", freezingData);
		return viewName;
	}
	
	@RequestMapping(value="/point.do"
			,method=RequestMethod.GET
			,produces = "text/plain;charset=UTF-8")
	public String point(Model model) throws Exception {
		log.debug("┌──────────────────────────────┐");
		log.debug("│ point()               		  │");
		log.debug("└──────────────────────────────┘");
		
		String viewName = "main/main_occur_info";
		
		List<Point> listPoint = pointService.fullTableScan();
		log.debug("┌ list ┐");
		for(Point point : listPoint) {
			log.debug("│ point : " + point);
		}
		log.debug("└");
		
		model.addAttribute("pointData", listPoint);
		
		return viewName;
	}	
}
