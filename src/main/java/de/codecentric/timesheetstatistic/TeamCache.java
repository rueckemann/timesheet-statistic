package de.codecentric.timesheetstatistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TeamCache {
	private static final String UNDEFINED_TEAM = "unknown";
	private Map<String,String> teamMap;
	
	public TeamCache() {
		teamMap = new ConcurrentHashMap<String,String>();
	}
	
	public void addMember(String member, String team) {
		teamMap.put(member, team);
	}
	
	public String getTeamForMember(String member) {
		return teamMap.getOrDefault(member, UNDEFINED_TEAM);
	}
	
}
