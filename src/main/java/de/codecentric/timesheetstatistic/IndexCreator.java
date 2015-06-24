package de.codecentric.timesheetstatistic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.codecentric.jira.JiraClient;
import de.codecentric.jira.TimesheetConfig;
import de.codecentric.jira.restapi.Author;
import de.codecentric.jira.restapi.JiraIssue;
import de.codecentric.jira.restapi.JiraIssueContainer;
import de.codecentric.jira.restapi.Team;
import de.codecentric.jira.restapi.TeamMember;
import de.codecentric.jira.restapi.Worklog;
import de.codecentric.jira.restapi.WorklogContainer;

@Import(TimesheetConfig.class)
public class IndexCreator implements CommandLineRunner {

	private static Logger log = Logger.getLogger(IndexCreator.class);
	
	@Autowired
    private JiraClient jiraClient;
    
    @Autowired
    TeamCache teamCache;
    
    @Autowired
    AuthorCache authorCache;
    
    public static void main(String args[]) {
        SpringApplication.run(IndexCreator.class, args);
    }
    
    @Override
    public void run(String... args) {
    	log.debug("initializing team cache");
    	initializeTeamCache();
    	
    	List<JiraIssue> tickets2Report = getRelevantTimesheetTickets();
        
        for(JiraIssue ticket: tickets2Report) {
        	createIndexForDocuments(getJSonDocumentsForTicket(ticket));
        } 
        System.exit(0);
    }

	private void initializeTeamCache() {
		List<Team> teams = jiraClient.getTeams();
		for (Team team : teams) {
			List<TeamMember> userForTeam = jiraClient.getMembersForTeam(team);
			for (TeamMember teamMember : userForTeam) {
				String userName = teamMember.getMember().getName();
				if(teamCache.containsUser(userName)) {
					log.warn("User: " + userName + " is in member of multiple teams: " + teamCache.getTeamForMember(userName) + ", " + team.getName());
				}
				teamCache.addMember(teamMember.getMember().getName(), team.getName());
			}
		}
	}

	private void createIndexForDocuments(List<String> jsonDocumentsForTicket) {
		Node node = NodeBuilder.nodeBuilder().client(true).node();
    	Client client = node.client();
    	for (String document : jsonDocumentsForTicket) {
    		client.prepareIndex("timesheet_entry", "worklog").setSource(document).execute().actionGet();
		}
    	client.close();
    	node.close();
	}

	private List<String> getJSonDocumentsForTicket(JiraIssue ticket) {
		List<String>jsonDocuments = new ArrayList<String>();
		
		ObjectMapper mapper = new ObjectMapper();
		WorklogContainer worklogContainer = jiraClient.getWorklogs(ticket.getKey());
		List<Worklog> worklogs = worklogContainer.getWorklogs();
		log.debug("adding " + worklogs.size() + " index entries for ticket " + ticket.getKey());
		for (Worklog worklog : worklogs) {
		    try {
		    	fixAuthorInWorklog(worklog);
		    	String team = teamCache.getTeamForMember(worklog.getAuthor().getName());
		    	if(team == null) log.warn("KEIN TEAM FÜR: " + worklog.getAuthor().getName());
		    			    	
		    	// create the document for elasticsearch
		    	IndexEntry indexEntry = new IndexEntry(ticket, worklog, team);		    	
		    	String jsonDocument = mapper.writeValueAsString(indexEntry);
		    	jsonDocuments.add(jsonDocument);
		    	//System.out.println(jsonDocument);
		    
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}			
		}
		return jsonDocuments;
	}

	private void fixAuthorInWorklog(Worklog worklog) {	
	    Author author = worklog.getAuthor();
		if(author.getDisplayName() == null) {
			String authorName = author.getName();
	        if(!authorCache.containsKey(authorName)) {
	        	Author correctAuthor = jiraClient.getAuthor(authorName);
		        authorCache.put(authorName, correctAuthor);
			}
	        worklog.setAuthor(authorCache.get(author.getName()));
	    }
	}

	private List<JiraIssue> getRelevantTimesheetTickets() {
		//TODO: get jql from properties file
		String jql = "project = \"Timesheet \" AND component not in (codecentric, \"codecentric AG\")";
		List<JiraIssue> result = new ArrayList<JiraIssue>();
		int currentIndex = 0;
		
		JiraIssueContainer issueContainer = jiraClient.getTicketsByJql(jql, currentIndex);
		log.debug("evaluating a total of  " + issueContainer.getTotal() + " timesheet tickets");
		result.addAll(issueContainer.getIssues());
		
		//TODO: pagenagion does not make sense when we store all issues in the result, maybe use a callback 
		while(currentIndex + issueContainer.getMaxResults() < issueContainer.getTotal()) {			
			currentIndex += issueContainer.getMaxResults();
			issueContainer = jiraClient.getTicketsByJql(jql, currentIndex);
			log.debug("evaluation IssueContainer: " + issueContainer);
			result.addAll(issueContainer.getIssues());
		}
		
		log.debug("Query contains " + issueContainer.getTotal() + " returning " + result.size() + " issues");
		return result;
	}
	
}