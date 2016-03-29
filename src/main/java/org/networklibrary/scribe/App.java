package org.networklibrary.scribe;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.networklibrary.core.config.ConfigManager;
import org.networklibrary.scribe.config.ScribeConfigManager;

/**
 * Hello world!
 *
 */
public class App 
{
	protected static final Logger log = Logger.getLogger(App.class.getName());
    
	public static void main( String[] args )
    {
    	Options options = new Options();
    	Option help = OptionBuilder.withDescription("Help message").create("help");
    	Option dbOp = OptionBuilder.withArgName("[URL]").hasArg().withDescription("Neo4j instance to scribe").withLongOpt("target").withType(String.class).create("db");
    	Option queryOp = OptionBuilder.withArgName("[QUERY]").hasArg().withDescription("Cypher query to execute:").withType(String.class).create("q");
    	Option typeOp = OptionBuilder.withArgName("[OUTPUT TYPE]").hasArg().withDescription("Output data type:").withType(String.class).create("ot");
    	Option qtypeOp = OptionBuilder.withArgName("[QUERY TYPE]").hasArg().withDescription("type of query").withType(String.class).create("qt");
    	Option extraOps = OptionBuilder.hasArg().withDescription("Extra configuration parameters for the execution").withType(String.class).create("x");
    	
    	options.addOption(help);
    	options.addOption(dbOp);
    	options.addOption(queryOp);
    	options.addOption(typeOp);
    	options.addOption(qtypeOp);
    	options.addOption(extraOps);
    	
    	CommandLineParser parser = new GnuParser();
        try {
            
            CommandLine line = parser.parse( options, args );
            
            if(line.hasOption("help") || args.length == 0){
            	HelpFormatter formatter = new HelpFormatter();
            	formatter.printHelp( "netlib-scribe [OPTIONS] [FILE]", options );
            	
            	return;
            }
            
            String db = null;
            if(line.hasOption("db")){
            	db = line.getOptionValue("db");
            }
            
            String type = null;
            if(line.hasOption("ot")){
            	type = line.getOptionValue("ot");
            }
            
            String query = null;
            if(line.hasOption("q")){
            	query = line.getOptionValue("q");
            }
            
            String queryType = null;
            if(line.hasOption("qt")){
            	queryType = line.getOptionValue("qt");
            }
                        
            List<String> extras = null;
            if(line.hasOption("x")){
            	extras = Arrays.asList(line.getOptionValues("x"));
            }
            
            List<String> outputFiles = line.getArgList();
            
            ScribeConfigManager confMgr = new ScribeConfigManager();
            
            Scribe scribe = new Scribe(db,type,query,queryType,extras,outputFiles,confMgr);
            scribe.execute();
            
        }
        catch( Exception exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            exp.printStackTrace();
        }
    }
}
