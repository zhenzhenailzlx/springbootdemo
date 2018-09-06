package com.zhenzhen.demo.springboot.common.result;
public class Result {

	//1 表示成功，0表示失败
	private String code;
	private Object data;
	private String message;
	
	
	
	public Result() {
		super();
	}

	public Result(String code) {
		super();
		this.code = code;
	}
	

	public Result(Object data) {
		super();
		this.code = "1";
		this.data = data;
	}




	public Result(String code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}



	public Result(String code, Object data, String message) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}
	
	
	public Result(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
