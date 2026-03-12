//package org.automation.factory;
//
//import com.pchf.client.util.DataValidationUtil;
//import com.pchf.client.util.db.impl.MongoDBHelper;
//import com.pchf.model.BulkFileIngestionTestData;
//import com.pchf.model.CommonInfo;
//import com.pchf.model.EventType;
//import com.pchf.model.TestEvent;
//import com.pchf.util.CommonUtils;
//import com.piramal.qa.logger.log.Log;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Objects;
//
//@Component("DbHelperEventHandler")
//public class DbHelperEvent implements Event {
//  @Autowired MongoDBHelper mongoDBHelper;
//  @Autowired private DataValidationUtil dataValidationUtil;
////  private Map<String, Object> dbRecord = new HashMap<>();
//
//  @PostConstruct
//  @Override
//  public void postConstruct() {
//    EventFactory.registerEvent(EventType.DB_HELPER, this);
//  }
//
//  @Override
//  public void performDataSetup(TestEvent event, CommonInfo commonInfo)
//      throws InterruptedException {}
//
//  @Override
//  public void executeEvent(
//      TestEvent event, CommonInfo commonInfo, BulkFileIngestionTestData bulkFileIngestionTestData)
//      throws InterruptedException {
//
//    Log.info("DB Helper operation: " + event.getOperation());
//
//    Query query = buildQuery(event.getQueryCriteria(), commonInfo);
//
//      switch (event.getOperation().toUpperCase()) {
//          case "FIND" -> mongoDBHelper.find(
//                  event.getDbPrefix(),
//                  query,
//                  Map.class,   // generic for automation
//                  event.getCollectionName()
//          );
//          case "FIND_ONE" -> mongoDBHelper.findOne(
//                  event.getDbPrefix(),
//                  query,
//                  Map.class,
//                  event.getCollectionName()
//          );
//          case "DELETE" -> mongoDBHelper.delete(
//                  event.getDbPrefix(),
//                  event.getCollectionName(),
//                  query
//          );
//          case "UPDATE" -> {
//              Update update = buildUpdate(event.getUpdateData());
//              mongoDBHelper.updateOne(
//                      event.getDbPrefix(),
//                      query,
//                      Map.class,
//                      event.getCollectionName(),
//                      update
//              );
//          }
//          case "UPSERT" -> {
//              Update upsertUpdate = buildUpdate(event.getUpdateData());
//              mongoDBHelper.upsert(
//                      event.getDbPrefix(),
//                      query,
//                      Map.class,
//                      event.getCollectionName(),
//                      upsertUpdate
//              );
//          }
//          case "INSERT" -> mongoDBHelper.insert(
//                  event.getDbPrefix(),
//                  event.getCollectionName(),
//                  event.getInsertDocPayload()
//          );
//          case "VALIDATE" -> {
//              Map<String, Object> dbRecord =
//                      mongoDBHelper.findOne(
//                              event.getDbPrefix(),
//                              query,
//                              Map.class,
//                              event.getCollectionName()
//                      );
//
//              if (dbRecord == null) {
//                  dbRecord = null;
//                  Log.info("No DB record found for criteria: " + event.getQueryCriteria());
//              }
//
//              commonInfo.setDbRecord(dbRecord);
//
//              Log.info("DB Record fetched for validation: " + dbRecord);
//          }
//          default -> throw new IllegalArgumentException(
//                  "Unsupported DB operation: " + event.getOperation()
//          );
//      }
//  }
//
//  @Override
//  public void validateInfo(TestEvent event, CommonInfo commonInfo) throws InterruptedException {
//    if (Objects.nonNull(event.getValidation())) {
//      dataValidationUtil.validate(
//              com.pchf.client.model.Event.builder()
//                      .description("Db Record Validation")
//                      .validation(event.getValidation())
//                      .response(commonInfo.getDbRecord())
//                      .build());
//    }
//  }
//
//  @Override
//  public void dataCleanUp(TestEvent event, CommonInfo commonInfo) {
//  }
//
//  //Helper Methods
////  private Query buildQuery(Map<String, Object> criteriaMap) {
////    Query query = new Query();
////
////    if (criteriaMap != null) {
////      criteriaMap.forEach((key, value) ->
////              query.addCriteria(Criteria.where(key).is(value))
////      );
////    }
////    return query;
////  }
//
//    private Query buildQuery(Map<String, Object> criteriaMap, CommonInfo commonInfo) {
//        Query query = new Query();
//
//        if (criteriaMap != null) {
//            criteriaMap.forEach((key, value) -> {
//                Object resolvedValue = CommonUtils.resolveDynamicValue(value, commonInfo);;
//                query.addCriteria(Criteria.where(key).is(resolvedValue));
//            });
//        }
//        return query;
//    }
//
//    private Update buildUpdate(Map<String, Object> updateMap) {
//    Update update = new Update();
//
//    if (updateMap != null) {
//      updateMap.forEach((key, value) -> {
//        if ("#now".equals(value)) {
//          update.set(key, System.currentTimeMillis());
//        } else {
//          update.set(key, value);
//        }
//      });
//    }
//    return update;
//    }
//}