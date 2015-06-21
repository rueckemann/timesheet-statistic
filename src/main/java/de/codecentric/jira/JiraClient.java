package de.codecentric.jira;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.codecentric.jira.restapi.JiraIssueContainer;
import de.codecentric.jira.restapi.Team;
import de.codecentric.jira.restapi.TeamMember;
import de.codecentric.jira.restapi.Author;
import de.codecentric.jira.restapi.WorklogContainer;

public class JiraClient {

    private static final String JIRA_RESTAPI_WORKLOGS = "/rest/api/2/issue/{issueID}/worklog";
    private static final String JIRA_RESTAPI_USER_PER_KEY = "/rest/api/2/user?key={userKey}";
    private static final String TEMPO_TEAMS_API = "/rest/tempo-teams/1/team/";
    private static final String TEMPO_TEAM_MEMBERS = "/rest/tempo-teams/2/team/{teamId}/member";
    private static final String JIRA_JQL_SEARCH = "/rest/api/2/search?jql={jql}";
    
    
    private String jiraUrl;
    private String username;
    private String password;
    
    public JiraClient(String jiraUrl, String username, String password) {
        this.jiraUrl = jiraUrl;
        this.username = username;
        this.password = password;
    }
    
    public List<Team> getTeams() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Team[]> responseEntity = restTemplate.exchange(jiraUrl + TEMPO_TEAMS_API, HttpMethod.GET, generateAuthRequest(), Team[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    public WorklogContainer getWorklogs(String issueID) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WorklogContainer> response = restTemplate.exchange(jiraUrl + JIRA_RESTAPI_WORKLOGS, HttpMethod.GET, generateAuthRequest(), WorklogContainer.class, issueID);
        WorklogContainer worklogContainer = response.getBody();
        return worklogContainer;
    }
    
    public JiraIssueContainer getTicketsByJql(String jql) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JiraIssueContainer> response = restTemplate.exchange(jiraUrl + JIRA_JQL_SEARCH, HttpMethod.GET, generateAuthRequest(), JiraIssueContainer.class, jql);
        JiraIssueContainer issueContainer = response.getBody();
        return issueContainer;
    }
    
    private HttpEntity<String> generateAuthRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + generateBase64Credentials());        
        return new HttpEntity<String>(headers);
    }
    
    private String generateBase64Credentials() {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        return base64Creds;
    }
    
    public Author getAuthor(String userKey) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Author> response = restTemplate.exchange(jiraUrl + JIRA_RESTAPI_USER_PER_KEY, HttpMethod.GET, generateAuthRequest(), Author.class, userKey);
        return response.getBody();
    }

	public List<TeamMember> getMembersForTeam(Team team) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TeamMember[]> responseEntity = restTemplate.exchange(jiraUrl + TEMPO_TEAM_MEMBERS, HttpMethod.GET, generateAuthRequest(), TeamMember[].class, team.getId());
        return Arrays.asList(responseEntity.getBody());
 	
	}

    
}
