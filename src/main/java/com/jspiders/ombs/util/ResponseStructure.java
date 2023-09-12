package com.jspiders.ombs.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseStructure<A> {
	private int statusCode;
	private String message;
	private A data;
}
