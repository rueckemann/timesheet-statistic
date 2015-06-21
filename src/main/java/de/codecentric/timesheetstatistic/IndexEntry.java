package de.codecentric.timesheetstatistic;

import java.text.SimpleDateFormat;
import java.util.List;

import de.codecentric.jira.restapi.JiraComponent;
import de.codecentric.jira.restapi.JiraIssue;
import de.codecentric.jira.restapi.Author;
import de.codecentric.jira.restapi.Worklog;

public class IndexEntry {

    // JiraIssue fields
	private String ticketKey;
	private String component;
    
	// worklog fields
//	private Author author;
	private String authorDisplayName;
    private String comment;
    private String created;
    private String id;
    private String timeSpent;
    private Integer timeSpentSeconds;
    private String updated;
    
    // the team the author belongs to
    private String team;
    
    // calculated fields
	private String worklogDate;
    
    public IndexEntry(JiraIssue issue, Worklog worklog, String team) {
    	this.ticketKey = issue.getKey();
    	this.component = getComponentAsString(issue.getFields().getComponents());
 //   	this.author = worklog.getAuthor();
    	this.authorDisplayName = worklog.getAuthor().getDisplayName();
    	this.comment = worklog.getComment();
    	this.created = worklog.getCreated();
    	this.id = worklog.getId();
    	this.worklogDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(worklog.getStarted());
    	this.timeSpent = worklog.getTimeSpent();
    	this.timeSpentSeconds = worklog.getTimeSpentSeconds();
    	this.updated = worklog.getUpdated();
    	this.team = team;
    }
       
	private String getComponentAsString(List<JiraComponent> components) {
		StringBuilder result = new StringBuilder();
		if(components != null) {
			for (JiraComponent jiraComponent : components) {
				result.append(jiraComponent);
			}
		}
		return result.toString();
	}



//	public Author getAuthor() {
//		return author;
//	}
//
//
//	public void setAuthor(Author author) {
//		this.author = author;
//	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getCreated() {
		return created;
	}


	public void setCreated(String created) {
		this.created = created;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTimeSpent() {
		return timeSpent;
	}


	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}


	public Integer getTimeSpentSeconds() {
		return timeSpentSeconds;
	}


	public void setTimeSpentSeconds(Integer timeSpentSeconds) {
		this.timeSpentSeconds = timeSpentSeconds;
	}


	public String getUpdated() {
		return updated;
	}


	public void setUpdated(String updated) {
		this.updated = updated;
	}


	public String getWorklogDate() {
		return worklogDate;
	}


	public void setWorklogDate(String worklogDate) {
		this.worklogDate = worklogDate;
	}


	public String getTeam() {
		return team;
	}


	public void setTeam(String team) {
		this.team = team;
	}


	public String getTicketKey() {
		return ticketKey;
	}


	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}



	public String getComponent() {
		return component;
	}



	public void setComponent(String component) {
		this.component = component;
	}

	public String getAuthorDisplayName() {
		return authorDisplayName;
	}

	public void setAuthorDisplayName(String authorDisplayName) {
		this.authorDisplayName = authorDisplayName;
	}
   
}
