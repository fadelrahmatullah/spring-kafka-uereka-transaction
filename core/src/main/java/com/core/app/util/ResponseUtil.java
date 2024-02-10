package com.core.app.util;

import org.springframework.stereotype.Component;

import com.core.app.constants.ResponseStatus;
import com.core.app.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ResponseUtil {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public <T> Response<T> generateResponseSuccess(String messageCode, T data, String... varValues) {
		Response<T> result = new Response<>(ResponseStatus.SUCCESS, dateFormat.format(new Date()));
		result.setMessageCode(messageCode);
		result.setMessage(String.join(", ", varValues));
		result.setData(data);

		return result;
	}

	public <T> Response<T> generateResponseSuccess(T data) {

		return this.generateResponseSuccess("MUSERR001INF", data);
	}

	public <T> Response<T> generateResponseError(String messageCode, T data, String... varValues) {

		Response<T> result = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
		result.setMessageCode(messageCode);
		result.setMessage(String.join(", ", varValues));
		result.setData(null);
		return result;

	}

	public Response<Object> generateResponseSuccess() {
		return this.generateResponseSuccess(null);
	}

	public Response<Object> generateResponseError(String messageCode, Exception ex) {
		Response<Object> result = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
		result.setMessageCode(messageCode);
		result.setMessage(ex.getMessage());

		return result;
	}

	public Response<Object> generateResponseError(String messageCode, String... varValues) {
		Response<Object> result = new Response<>(ResponseStatus.ERROR, dateFormat.format(new Date()));
		result.setMessageCode(messageCode);
		result.setMessage(String.join(", ", varValues));

		return result;
	}
}