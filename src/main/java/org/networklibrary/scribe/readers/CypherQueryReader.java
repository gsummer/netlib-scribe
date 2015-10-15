package org.networklibrary.scribe.readers;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.networklibrary.scribe.writers.CypherResultWriter;

public class CypherQueryReader {

	private GraphDatabaseService graph;
	private CypherResultWriter cypherWriter;

	public CypherQueryReader( GraphDatabaseService graph, CypherResultWriter cypherWriter){
		this.graph = graph;
		this.cypherWriter = cypherWriter;
		
	}

	public void executeCypher(String query){

		try (Transaction tx = graph.beginTx()){

			Result res = graph.execute(query);
			cypherWriter.setColumns(res.columns());
			
			while(res.hasNext()){
				cypherWriter.addRow(res.next());
			}
			res.close();
			tx.success();
		} catch (QueryExecutionException e){
			System.out.println(e);
		}
	}
}
