package com.techelevator.projects.model.jdbc;

import java.util.ArrayList; 
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List <Project> projectList = new ArrayList<>();
		String sqlAllActiveProjects = "SELECT * FROM project "
				+ "WHERE to_date >= current_date OR to_date IS NULL";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllActiveProjects);
		while (results.next()) {
			Project nextProject = new Project();
			nextProject = mapRowToProject(results);
			projectList.add(nextProject);
		}
		return projectList; 
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
	String sqlRemoveEmployeeFromProject = "DELETE FROM project_employee WHERE " +
	"project_id = ? AND employee_id = ? ";
	jdbcTemplate.update(sqlRemoveEmployeeFromProject, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sqlAddEmployeeToProject = "INSERT INTO project_employee (project_id, employee_id) " +
				"VALUES (?, ?)";
				jdbcTemplate.update(sqlAddEmployeeToProject, projectId, employeeId);
				}
	
	
	@Override
	public Project addProject(Project newProject) {
	String sqlInsertProject = "INSERT INTO project(project_id, "
			+ "name, from_date, to_date) " +
			"VALUES(?, ?, ?, ?)";
		newProject.setId(Long.valueOf(getNextProjectId()));
				
		jdbcTemplate.update(sqlInsertProject,
		newProject.getId(),
		newProject.getName(),
		newProject.getStartDate(),
		newProject.getEndDate());
		return newProject;		
}

	
	private int getNextProjectId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('seq_project_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Uhoh!  Something went wrong while getting the next id!");
		}
	} 
	private Project mapRowToProject(SqlRowSet results) {
		Project project = new Project();
		project.setId((results.getLong("project_id")));  
		project.setName(results.getString("name"));
		if(results.getDate("from_date") != null) {
			project.setStartDate(results.getDate("from_date").toLocalDate());
			}
		if(results.getDate("to_date") != null) {
			project.setEndDate(results.getDate("to_date").toLocalDate());
		}
		return project;
	}
}
