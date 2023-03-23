package com.tth.management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Document(indexName = "event")
@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable {

    @Id
    @org.springframework.data.annotation.Id
    @Field(type = FieldType.Keyword)
    private String uuid;

    @Column(name = "event_type")
    private EventType eventType;

    @Field(type = FieldType.Text)
    private String place;

    @Column(name = "object_type")
    private ObjectType objectType;

    @Column(name = "source_type")
    private SourceType sourceType;

    private String image;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "is_newest")
    private boolean isNewest;

    @Column(name = "is_delete")
    private boolean isDelete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    private Status status;

}
