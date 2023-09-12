package com.jspiders.ombs.util;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorStructure {
	private int statusCode;
	private String message;
	private String rootCause;
	private LocalDateTime dateTime;

}
