package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate; 

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
 
	@Override
	public List<Department> getAllDepartments() {
		List <Department> departmentList = new ArrayList<>();
		String sqlAllDepartments = "SELECT * FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllDepartments);
		while (results.next()) {
			Department nextDepartment = new Department();
			nextDepartment = mapRowToDepartment(results);
			departmentList.add(nextDepartment);
		}
		return departmentList;
	} 

	@Override 
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List <Department> departmentList = new ArrayList<>();
		String sqlSearchDepartmentByName = "SELECT name, department_id " +
		     "FROM department WHERE name LIKE  ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchDepartmentByName,"%" +  nameSearch + "%");
		if (results.next()) {
			Department nextDepartment = new Department();
			nextDepartment = mapRowToDepartment(results);
			departmentList.add(nextDepartment);		
			}
		return departmentList;
	} 
	

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sqlUpdateDepartment = "UPDATE department SET name = ? WHERE " +
				"department_id = ?";
					jdbcTemplate.update(sqlUpdateDepartment,
							updatedDepartment.getName(),
							updatedDepartment.getId());		
		
	}
		
  
	@Override
	public Department createDepartment(Department newDepartment) {
		String sqlInsertDepartment = "INSERT INTO department(department_id, name) " +
				"VALUES(?, ?)";
					newDepartment.setId(Long.valueOf(getNextDepartmentId()));
					
					jdbcTemplate.update(sqlInsertDepartment,
						newDepartment.getId(),
			 			newDepartment.getName());
						return newDepartment;
					}
				
	private int getNextDepartmentId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('seq_department_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Uhoh!  Something went wrong while getting the next id!");
		}
	} 

	@Override
	public Department getDepartmentById(Long id) {
		Department department = null;
		String sqlFindDepartmentById = "SELECT department_id, name " + 
		"FROM department WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindDepartmentById, id);
			if(results.next()) {
				department = mapRowToDepartment(results);
			}
			return department;		
	}
	
	private Department mapRowToDepartment(SqlRowSet results) {
		Department department = new Department();
		department.setId((results.getLong("department_id")));  
		department.setName(results.getString("name"));
		
		return department;
	}

}
