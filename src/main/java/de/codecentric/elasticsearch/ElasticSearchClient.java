package de.codecentric.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchClient {
	private Client client;
	
	public ElasticSearchClient() {
		Node node = NodeBuilder.nodeBuilder().client(true).node();
		client = node.client();
	}
	
	public boolean createIndexIfNotExists(String indexName, String type, String typeMapping) {
    	if(!client.admin().indices().exists(new IndicesExistsRequest(indexName)).actionGet().isExists()) {
    		client.admin().indices().create(new CreateIndexRequest(indexName).mapping(type, typeMapping)).actionGet();
    		return true;
    	}
    	return false;
	}
	
	public boolean deleteIndex(String indexName) {
        return client.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet().isAcknowledged();
	}
	
	public void close() {
		client.close();
	}
	
}
