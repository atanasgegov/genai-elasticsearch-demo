package com.bosch.demo.genaies.request;

import com.bosch.demo.genaies.model.Request;
import com.google.gson.JsonObject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.nodes.NodesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.Stats;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class NodesStatsRequest implements Request<JsonObject> {

	private static final String JSON_PROPERTY_STATUS = "status";
	private static final String JSON_PROPERTY_NODEID = "nodeId";

	private ElasticsearchClient elasticsearchClient;
	
	@Override
	public JsonObject execute() {
		
    	JsonObject result = new JsonObject();
    	try {
    		NodesStatsResponse nodesStatsResponse = elasticsearchClient.nodes().stats();
    		// Loop through each node and retrieve process stats
        	JsonObject root = new JsonObject();
        	
            for (String nodeId : nodesStatsResponse.nodes().keySet()) {

            	Stats stat = nodesStatsResponse.nodes().get(nodeId);

                // Retrieve the process stats
        		JsonObject nodesStatsProcess = getNodeStatsProcess(stat, nodeId);
        		root.add("process-"+nodeId, nodesStatsProcess);
            	
                // Retrieve the OS stats
        		JsonObject nodesStatsOs = getNodeStatsOS(stat, nodeId);
        		root.add("os-"+nodeId, nodesStatsOs);

                // Retrieve the JVM stats
        		JsonObject nodesStatsJvm = getNodeStatsJVM(stat, nodeId);
        		root.add("jvm-"+nodeId, nodesStatsJvm);
            }
            result.add("nodesStats", root);
    	} catch (Exception e) {

        	result.addProperty(JSON_PROPERTY_STATUS, "Something wrong happened trying to get nodes stats.");

        	log.error(result.toString(), e);
        }
    	
    	return result;
	}
	
	@Override
	public String getName() {
		return "GET /_nodes/stats";
	}

    private JsonObject getNodeStatsProcess(Stats stat, String nodeId) {
    	
        // Retrieve the process stats
    	co.elastic.clients.elasticsearch.nodes.Process processStats = stat.process();
		JsonObject nodesStatsProcess = new JsonObject();
        if (processStats != null) {
        	nodesStatsProcess.addProperty(JSON_PROPERTY_NODEID, nodeId);
        	nodesStatsProcess.addProperty("timestamp", processStats.timestamp());
        	nodesStatsProcess.addProperty("openFileDescriptors", processStats.openFileDescriptors());
        	nodesStatsProcess.addProperty("maxFileDescriptors", processStats.maxFileDescriptors());
            if (processStats.cpu() != null) {
            	nodesStatsProcess.addProperty("cpuPercent", processStats.cpu().percent());
            	nodesStatsProcess.addProperty("totalCpuTime", processStats.cpu().totalInMillis());
            }
            if (processStats.mem() != null) {
            	nodesStatsProcess.addProperty("totalVirtualMemory", processStats.mem().totalVirtualInBytes());
            }
        } else {
            log.debug("No process stats available for the node: {}", nodeId);
        }

        return nodesStatsProcess;
    }
    
    private JsonObject getNodeStatsOS(Stats stat, String nodeId) {
    	
    	co.elastic.clients.elasticsearch.nodes.OperatingSystem osStats = stat.os();
		JsonObject nodesStatsOs = new JsonObject();
        if (osStats != null) {
        	nodesStatsOs.addProperty(JSON_PROPERTY_NODEID, nodeId);
        	nodesStatsOs.addProperty("timestamp", osStats.timestamp());
            if (osStats.cpu() != null) {
            	nodesStatsOs.addProperty("cpuPercent", osStats.cpu().percent());
            	if (osStats.cpu().loadAverage() != null && osStats.cpu().loadAverage().containsKey("1m")) {
            		nodesStatsOs.addProperty("LoadAverage(1m)", osStats.cpu().loadAverage().get("1m"));
            	}
            	nodesStatsOs.addProperty("totalCpuTime", osStats.cpu().totalInMillis());
            }
            if (osStats.mem() != null) {
            	nodesStatsOs.addProperty("MemoryFree(bytes)", osStats.mem().freeInBytes());
            	nodesStatsOs.addProperty("MemoryUsed(bytes)", osStats.mem().usedInBytes());
            	nodesStatsOs.addProperty("MemoryTotal(bytes)", osStats.mem().totalInBytes());                    }
            	nodesStatsOs.addProperty("totalVirtualMemory", osStats.mem().totalVirtualInBytes());
        } else {
            log.debug("No OS stats available for the node: {}", nodeId);
        }

        return nodesStatsOs;
    }
    
    private JsonObject getNodeStatsJVM(Stats stat, String nodeId) {

    	co.elastic.clients.elasticsearch.nodes.Jvm jvmStats = stat.jvm();
		JsonObject nodesStatsJvm = new JsonObject();
        if (jvmStats != null) {
        	nodesStatsJvm.addProperty(JSON_PROPERTY_NODEID, nodeId);
        	nodesStatsJvm.addProperty("Uptime(ms)", jvmStats.uptimeInMillis());
        	nodesStatsJvm.addProperty("HeapUsed(bytes)", jvmStats.mem().heapUsedInBytes());
        	nodesStatsJvm.addProperty("HeapCommitted(bytes)", jvmStats.mem().heapCommittedInBytes());
        	nodesStatsJvm.addProperty("Non-HeapUsed(bytes)", jvmStats.mem().nonHeapUsedInBytes());
        	nodesStatsJvm.addProperty("Non-HeapCommitted(bytes)", jvmStats.mem().nonHeapCommittedInBytes());
        } else {
            log.debug("No JVM stats available for the node: {}", nodeId);
        }
        
        return nodesStatsJvm;
    }
	
}
