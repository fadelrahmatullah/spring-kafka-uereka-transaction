package com.core.app.response;

import com.core.app.constants.ResponseStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response<T> {

	private ResponseStatus status;

	private String messageCode;

	private String message;

	private String processFinishDate;

	private T data;
	
	public Response(ResponseStatus status, String processFinishDate) {
		this.status = status;
		this.processFinishDate = processFinishDate;
	}
	
	
}
