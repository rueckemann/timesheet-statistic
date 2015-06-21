package de.codecentric.jira.restapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
	private String id;
	private String key;
	private JiraFields fields;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public JiraFields getFields() {
		return fields;
	}
	public void setFields(JiraFields fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		return "JiraIssue [id=" + id + ", key=" + key + ", components=" + fields.getComponents() + "]";
	}
}
