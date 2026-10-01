package vn.edu.hcmute.bookstore_24110202.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormResult_24110202<T> {
    private T form;
    private Map<String, String> fieldErrors = new LinkedHashMap<>();
    private String globalError;

    public FormResult_24110202(T form) {
        this.form = form;
    }

    public T getForm() {
        return form;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public String getGlobalError() {
        return globalError;
    }

    public void addError(String field, String message) {
        fieldErrors.put(field, message);
    }

    public void setGlobalError(String globalError) {
        this.globalError = globalError;
    }

    public boolean isValid() {
        return fieldErrors.isEmpty() && globalError == null;
    }
}
