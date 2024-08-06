package com.acorn.doma.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.doma.cmn.WorkDiv;
import com.acorn.doma.domain.Accident;

@Mapper
public interface AccMapper extends WorkDiv<Accident>{
	
	int dataInsert(Accident inVO) throws SQLException;
	
	int doDeleteAll() throws SQLException;
	
	int countAll() throws SQLException;
	
	List<Accident> fullTableScan() throws SQLException;
	
}
