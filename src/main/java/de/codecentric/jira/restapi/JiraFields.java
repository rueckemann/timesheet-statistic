package de.codecentric.jira.restapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraFields {
	private List<JiraComponent> components;

	public List<JiraComponent> getComponents() {
		return components;
	}

	public void setComponents(List<JiraComponent> components) {
		this.components = components;
	}
	
}
