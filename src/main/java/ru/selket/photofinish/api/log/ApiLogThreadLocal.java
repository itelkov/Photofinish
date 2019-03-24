package ru.selket.photofinish.api.log;

public class ApiLogThreadLocal {

    private static final ThreadLocal<ApiLog> threadLocalScope = new  ThreadLocal<>();

    public static ApiLog get() {
        return threadLocalScope.get();
    }

    public static void set(ApiLog apiLog) {
        threadLocalScope.set(apiLog);
    }
}
