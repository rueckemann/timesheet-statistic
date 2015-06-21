package de.codecentric.jira.restapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueContainer {

    private String maxResults;
    private String startAt;
    private String total;
    private List<JiraIssue> issues;


    public String getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

	public List<JiraIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<JiraIssue> issues) {
		this.issues = issues;
	}

}
