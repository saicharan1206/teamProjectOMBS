package com.jspiders.ombs.dto;

import lombok.Data;

@Data
public class MessageData {
	
	private String to;
	private String subject;
	private String text;
	private String senderName;
	private String senderAddress;
	
}
