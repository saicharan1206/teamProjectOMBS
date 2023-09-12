package com.jspiders.ombs.util;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditing 
{
	@CreatedBy
	String createdBy;
	@CreatedDate
	LocalDateTime createdDate;
	@LastModifiedBy
	String lastmodifiedby;
	@LastModifiedDate
	LocalDateTime lastmodifieddate;
}
