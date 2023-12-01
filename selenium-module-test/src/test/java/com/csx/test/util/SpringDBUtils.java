package com.csx.test.util;

import com.csx.springConfig.annotation.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Sorts.ascending;

@Page
public class SpringDBUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDBUtils.class);
    public static final String ERROR_MSG = "Some error has occurred while performing operation::{}";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    MongoTemplate mongoTemplate;


    /**
     * ------------------------------- Fetching data in the form of List of Map  from database using JdbcTemplate
     * --------------------------------------
     */
    public List<Map<String, Object>> FetchDatabaseRecords(String query){
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
        return results;
    }

    public List<String> getcollection(String collectionName, Bson pipeline) {
        List<String> jsonString = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
            LOGGER.info("record: " + collection.getNamespace().getCollectionName());

            @SuppressWarnings("unchecked")
            FindIterable<Document> fi = collection.find(pipeline);
            MongoCursor<Document> cursor = fi.iterator();
            try {
                while (cursor.hasNext()) {
                    jsonString.add(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("exception occured while collections from DB ", e.getLocalizedMessage());
        }
        return jsonString;
    }

    public List<String> getAllSiteDetailsFromDB(String collectionName,String filed,String fieldValues,String reqField) throws JSONException {
        List<String> jsonString = new ArrayList<>();
        List<String> reqValues = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
            LOGGER.info("record: " + collection.getNamespace().getCollectionName());

            FindIterable<Document> fi = collection.find(Filters.eq(filed, fieldValues.trim()));
            MongoCursor<Document> cursor = fi.iterator();
            try {
                while (cursor.hasNext()) {
                    jsonString.add(cursor.next().toJson());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("exception occured while collections from DB ", e.getLocalizedMessage());
            }finally {
                cursor.close();
            }
           for (String str : jsonString)
            {
                JSONObject jobject = new JSONObject(str);
                reqValues.add(jobject.getString(reqField));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while getting data from DB");
        }
        return reqValues;
    }

    public List<String> getcollectionForMultipleRecords(String collectionName, Bson pipeline, String key1, String key2, int iD, String status) {
        List<String> jsonString = new ArrayList<>();
       try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
            LOGGER.info("record: " + collection.getNamespace().getCollectionName());
            BasicDBObject andQuery = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<>();
            obj.add(new BasicDBObject(key1, iD));
            obj.add(new BasicDBObject(key2, status));
            andQuery.put("$and", obj);
            @SuppressWarnings("unchecked")
            FindIterable<Document> fi = collection.find(andQuery);
            MongoCursor<Document> cursor = fi.iterator();
            try {
                while (cursor.hasNext()) {
                    jsonString.add(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("exception occured while collections from DB ", e.getLocalizedMessage());
        }
        return jsonString;
    }

    public List<String> getDataWithQuery(){
        List<String> jsonString = new ArrayList<>();
        BasicDBObject document = new BasicDBObject();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.put("workOrderType").is("FRA").
                put("testType").is("Grounds").
                put("frequencyInDays").is(90).
                put("milepost.begin").greaterThan(600).
                put("milepost.end").lessThan(700);

        document.putAll(queryBuilder.get());
        MongoCollection<Document> collection = mongoTemplate.getCollection("inspectionAssetInformationLatest");

        FindIterable<Document> fi = collection.find(document).sort(ascending("milepost.begin"));
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while (cursor.hasNext()) {
                jsonString.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        return jsonString;
    }

}
