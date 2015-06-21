package de.codecentric.timesheetstatistic;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import de.codecentric.jira.restapi.Author;

@Component
public class AuthorCache extends ConcurrentHashMap<String,Author> {
	private static final long serialVersionUID = 1L;

	public AuthorCache() {
		super();
	}
}
