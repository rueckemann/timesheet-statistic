package de.codecentric.timesheetstatistic;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
    public static void main(String[] args) throws JsonProcessingException
    {

    	Node node = NodeBuilder.nodeBuilder().client(true).node();
    	Client client = node.client();
   	
    	// instance a json mapper
    	ObjectMapper mapper = new ObjectMapper(); // create once, reuse
    	
    	// generate json
    	MyBean vertragDus = new MyBean(new Schluessel("12345", "DUS"));
    	MyBean vertragLux = new MyBean(new Schluessel("9876", "LUX"));

    	String jsonDus = mapper.writeValueAsString(vertragDus);
    	String jsonLux = mapper.writeValueAsString(vertragLux);
    	
    	IndexResponse response = client.prepareIndex("vertraege_dus", "vertrag").setId(vertragDus.toString()).setSource(jsonDus).execute().actionGet();
    	System.out.println("index:" + response.getIndex() + " type:" + response.getType() + " id:" + response.getId() + " version:" + response.getVersion());
    	IndexResponse resp1 = client.prepareIndex("vertraege_lux", "vertrag").setId(vertragLux.toString()).setSource(jsonLux).execute().actionGet();
    	System.out.println("index:" + resp1.getIndex() + " type:" + resp1.getType() + " id:" + resp1.getId() + " version:" + resp1.getVersion());
    	
//    	DeleteResponse resp = client.prepareDelete("vertraege", "vertrag", "WJqhiw1mQ665Gvw3h2jMkA")
//    	        .setOperationThreaded(false)
//    	        .execute()
//    	        .actionGet();
    	
    	TermQueryBuilder termQuery = QueryBuilders.termQuery("schluessel.nummer", "12345");
    	System.out.println("Query: " + termQuery.toString());
    	SearchResponse search = client.prepareSearch("vertraege", "vertraege_dus", "vertraege_lux")
    	        .setTypes("vertrag")
    	        .setQuery(termQuery)
    	        .execute()
    	        .actionGet(); 
    	
    	System.out.println("total hits: " + search.getHits().getTotalHits());
    	for(SearchHit hit : search.getHits().getHits()) {
    		System.out.println(hit.getSourceAsString());
    	}
    	

    	RegexpQueryBuilder query = QueryBuilders.regexpQuery("schluessel.standort", "DU*");
    	System.out.println(query.toString());
    	SearchResponse search2 = client.prepareSearch("vertraege_dus", "vertraege_lux").
    			setTypes("vertrag").
    			setQuery(query).
    			execute().
    			actionGet();
       	System.out.println("total hits: " + search2.getHits().getTotalHits());
   
    	client.close();
       
    }
    
}
class MyBean {
	private Schluessel schluessel;
	
	public Schluessel getSchluessel() {
		return schluessel;
	}
	public void setSchluessel(Schluessel schluessel) {
		this.schluessel = schluessel;
	}
	public MyBean() {}
	public MyBean(Schluessel schluessel) {
		this.schluessel = schluessel;
	}
	
	public String toString() {
		return this.schluessel.getStandort() + "_" + this.schluessel.getNummer();
	}
}

class Schluessel {
	private String nummer;
	private String standort;
	
	public Schluessel() {}
	
	public Schluessel(String nummer, String standort) {
		this.nummer = nummer;
		this.standort = standort;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public String getStandort() {
		return standort;
	}

	public void setStandort(String standort) {
		this.standort = standort;
	}
}

