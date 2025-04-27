package com.bosch.demo.genaies.request;

import java.io.IOException;

import com.bosch.demo.genaies.model.Request;
import com.google.gson.JsonObject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class ClusterHealthRequest implements Request<JsonObject> {

	private static final String JSON_PROPERTY_STATUS = "status";

	private ElasticsearchClient elasticsearchClient;

	@Override
	public JsonObject execute() {
		JsonObject result = new JsonObject();
        try {
        	HealthResponse clusterHealth = elasticsearchClient.cluster().health();

            JsonObject root = new JsonObject();
            root.addProperty("cluster_name", clusterHealth.clusterName());
            root.addProperty(JSON_PROPERTY_STATUS, clusterHealth.status().name());
            root.addProperty("timed_out", clusterHealth.timedOut());
            root.addProperty("number_of_nodes", clusterHealth.numberOfNodes());
            root.addProperty("number_of_data_nodes", clusterHealth.numberOfDataNodes());
            root.addProperty("active_primary_shards", clusterHealth.activePrimaryShards());
            root.addProperty("active_shards", clusterHealth.activeShards());
            root.addProperty("relocating_shards", clusterHealth.relocatingShards());
            root.addProperty("initializing_shards", clusterHealth.initializingShards());
            root.addProperty("unassigned_shards", clusterHealth.unassignedShards());
            root.addProperty("active_shards_percent_as_number", clusterHealth.activeShardsPercentAsNumber());
            root.addProperty("delayed_unassigned_shards", clusterHealth.delayedUnassignedShards());
            root.addProperty("number_of_in_flight_fetch", clusterHealth.numberOfInFlightFetch());
            root.addProperty("number_of_pending_tasks", clusterHealth.numberOfPendingTasks());
            root.addProperty("task_max_waiting_in_queue_millis", clusterHealth.taskMaxWaitingInQueueMillis());

            result.add("clusterHealth", root);
            return result;
        } catch (IOException e) {

            log.error("Something wrong happened trying to get cluster health.", e);

            result.addProperty(JSON_PROPERTY_STATUS, "ES connection not available");
            result.addProperty("message", "problem with ES connection");
            return result;
        }
	}
	
	@Override
	public String getName() {
		return "GET /_cluster/health";
	}}
