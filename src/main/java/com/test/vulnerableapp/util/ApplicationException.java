package com.test.vulnerableapp.util;

public class ApplicationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private int error_id;
	public ApplicationException() {
        super();
    }

    public ApplicationException(int error_id, String message) {
        super(message);
        this.error_id = error_id;
    }

    public ApplicationException(int error_id, String message, Throwable cause) {
        super(message, cause);
        this.error_id = error_id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

	public int getError_id() {
		return error_id;
	}

	public void setError_id(int error_id) {
		this.error_id = error_id;
	}
}