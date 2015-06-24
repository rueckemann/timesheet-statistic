package de.codecentric.timesheetstatistic;

import java.text.SimpleDateFormat;
import java.util.List;

import de.codecentric.jira.restapi.JiraComponent;
import de.codecentric.jira.restapi.JiraIssue;
import de.codecentric.jira.restapi.Worklog;

public class IndexEntry {

	private JiraIssue jiraIssue;
	private Worklog worklog;
    
    // the team the author belongs to
    private String team;
    
    public IndexEntry(JiraIssue issue, Worklog worklog, String team) {
    	this.jiraIssue = issue;
    	this.worklog = worklog;
    	// replace blanks in team names for the index entry. Otherwise kibana will show two teams for Novi Sad
    	this.team = team.replaceAll(" ", "");
    }
       
	public String getComment() {
		return worklog.getComment();
	}

	public String getCreated() {
		return worklog.getCreated();
	}

	public String getId() {
		return worklog.getId();
	}

	public String getTimeSpent() {
		return worklog.getTimeSpent();
	}

	public Integer getTimeSpentSeconds() {
		return worklog.getTimeSpentSeconds();
	}

	public String getUpdated() {
		return worklog.getUpdated();
	}

	public String getWorklogDate() {
		return worklog.getStarted() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(worklog.getStarted());
	}

	public String getTeam() {
		return team;
	}

	public String getTicketKey() {
		return this.jiraIssue.getKey();
	}

	public String getComponent() {
		return getComponentAsString(jiraIssue.getFields().getComponents());
	}

	public String getAuthorDisplayName() {
		return worklog.getAuthor().getDisplayName();
	}
	
	public String getAuthorName() {
		return worklog.getAuthor().getName();
	}

	public Double getTimeSpentHours() {
		return this.getTimeSpentSeconds() == null ? null : new Double(this.getTimeSpentSeconds() / 60.0 / 60.0);
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
}
