package de.codecentric.jira.restapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueContainer {

    private Integer maxResults;
    private Integer startAt;
    private Integer total;
    private List<JiraIssue> issues;


    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

	public List<JiraIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<JiraIssue> issues) {
		this.issues = issues;
	}

	@Override
	public String toString() {
		return "JiraIssueContainer [maxResults=" + maxResults + ", startAt=" + startAt + ", total=" + total
				+ ", issues=" + issues + "]";
	}

}
