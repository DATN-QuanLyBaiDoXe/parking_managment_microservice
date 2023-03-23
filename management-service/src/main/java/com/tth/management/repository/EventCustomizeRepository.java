package com.tth.management.repository;

import com.tth.management.model.Event;
import com.tth.management.model.ObjectType;
import com.tth.management.model.Status;
import com.tth.management.model.dto.EventPagingDTO;
import com.tth.management.model.dto.ReportDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventCustomizeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCustomizeRepository.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public EventCustomizeRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        } else {
            this.sessionFactory = (SessionFactory) factory.unwrap(SessionFactory.class);
        }
    }

    private Session openSession() {
        Session session = this.sessionFactory.openSession();
        return session;
    }

    private void closeSession(Session session) {
        if (session.isOpen()) {
            session.disconnect();
            session.close();
        }

    }

    public EventPagingDTO getAllEvent(Integer page, Integer size, List<Integer> eventTypeList, List<Integer> objectTypeList, List<Integer> sourceTypeList, Date startDate, Date endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Session session = openSession();
        try {
            StringBuilder queryString = new StringBuilder();
            StringBuilder queryCount = new StringBuilder();
            queryString.append("SELECT * FROM event WHERE is_newest = true AND created_date >= '").append(format.format(startDate)).append("' AND created_date <= '").append(format.format(endDate)).append("' ");
            queryCount.append("SELECT count(*) FROM event WHERE is_newest = true AND created_date >= '").append(format.format(startDate)).append("' AND created_date <= '").append(format.format(endDate)).append("' ");
            if (eventTypeList != null && !eventTypeList.isEmpty()) {
                String eventType = "";
                for (Integer type : eventTypeList) {
                    eventType += type + ",";
                }
                queryString.append(" AND event_type IN (").append(eventType.substring(0, eventType.length() - 1)).append(")");
                queryCount.append(" AND event_type IN (").append(eventType.substring(0, eventType.length() - 1)).append(")");
            }
            if (eventTypeList != null && !objectTypeList.isEmpty()) {
                String objectType = "";
                for (Integer type : objectTypeList) {
                    objectType += type + ",";
                }
                queryString.append(" AND object_type IN (").append(objectType.substring(0, objectType.length() - 1)).append(")");
                queryCount.append(" AND object_type IN (").append(objectType.substring(0, objectType.length() - 1)).append(")");
            }
            if (sourceTypeList != null && !sourceTypeList.isEmpty()) {
                String sourceType = "";
                for (Integer type : sourceTypeList) {
                    sourceType += type + ",";
                }
                queryString.append(" AND source_type IN (").append(sourceType.substring(0, sourceType.length() - 1)).append(")");
                queryCount.append(" AND source_type IN (").append(sourceType.substring(0, sourceType.length() - 1)).append(")");
            }
            queryString.append(" ORDER BY created_date DESC ");
            queryString.append(" LIMIT ").append(size).append(" OFFSET ").append(page * size);
            List<Event> eventList = session.createNativeQuery(queryString.toString(), Event.class).getResultList();
            Object countResult = session.createNativeQuery(queryCount.toString()).getSingleResult();
            Long count = 0L;
            if(countResult != null){
                count = Long.parseLong(countResult.toString());
            }
            EventPagingDTO eventPagingDTO = new EventPagingDTO();
            eventPagingDTO.setEventList(eventList);
            eventPagingDTO.setTotal(count);
            return eventPagingDTO;
        } finally {
            closeSession(session);
        }
    }

    public void eventPartitionMonth(String table) {
        Session session = this.openSession();

        try {
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
            String month = format.format(date);
            String fromPartitonValue = getPartitionValueOfCurrentMonth();
            String toPartitonValue = getPartitionValueOfNextMonth();
            Transaction transaction = session.beginTransaction();
            String partitionName = table + "_" + month;
            String strSql = " CREATE TABLE " + partitionName + " PARTITION OF " + table + " FOR VALUES FROM (TO_TIMESTAMP('" + fromPartitonValue +
                    "', 'YYYY-MM-DD HH24:MI:SS'))  TO (TO_TIMESTAMP('" + toPartitonValue + "', 'YYYY-MM-DD HH24:MI:SS')) ";
            Query query = session.createNativeQuery(strSql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            LOGGER.error(exception.toString());
        } finally {
            this.closeSession(session);
        }
    }

    public static String getPartitionValueOfCurrentMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString() + " 00:00:00";
    }

    public static String getPartitionValueOfNextMonth() {
        return LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).toString() + " 00:00:00";
    }

    public List<ReportDTO> reportEventByObject(String filterTimeLevel) {
        Session session = openSession();
        try {
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT object_type, count(object_type) FROM event ");
            queryString.append("WHERE date_trunc('").append(filterTimeLevel).append("', created_date) = date_trunc('").append(filterTimeLevel).append("', now()) ");
            queryString.append("GROUP BY object_type ");
            Query query = session.createNativeQuery(queryString.toString());
            List<Object[]> queryResultList = query.getResultList();
            List<ReportDTO> reportDTOList = queryResultList.stream().map(item -> {
                ReportDTO reportDTO = new ReportDTO();
                Integer code = Integer.parseInt(item[0].toString());
                reportDTO.setCode(code);
                reportDTO.setName(ObjectType.of(code).getDescription());
                reportDTO.setTotal(Integer.parseInt(item[1].toString()));
                return reportDTO;
            }).collect(Collectors.toList());
            return reportDTOList;
        } finally {
            closeSession(session);
        }
    }

    public List<ReportDTO> reportEventByStatus(String filterTimeLevel) {
        Session session = openSession();
        try {
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT status, count(status) FROM event ");
            queryString.append("WHERE date_trunc('").append(filterTimeLevel).append("', created_date) = date_trunc('").append(filterTimeLevel).append("', now()) ");
            queryString.append("GROUP BY status ");
            Query query = session.createNativeQuery(queryString.toString());
            List<Object[]> queryResultList = query.getResultList();
            List<ReportDTO> reportDTOList = queryResultList.stream().map(item -> {
                ReportDTO reportDTO = new ReportDTO();
                Integer code = Integer.parseInt(item[0].toString());
                reportDTO.setCode(code);
                reportDTO.setName(Status.of(code).getType());
                reportDTO.setTotal(Integer.parseInt(item[1].toString()));
                return reportDTO;
            }).collect(Collectors.toList());
            return reportDTOList;
        } finally {
            closeSession(session);
        }
    }
}
