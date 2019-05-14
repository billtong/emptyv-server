package com.empty.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class MySessionContext {

    private static MySessionContext instance;
    private Map<String, HttpSession> sessionMap;

    private MySessionContext() {
        sessionMap = new HashMap<>();
    }

    public Map<String, HttpSession> getSessionMap() {
        return this.sessionMap;
    }

    /*
     * 从外部获取这个唯一的MySessionContext
     */
    public static MySessionContext getInstance() {
        if (instance == null) {
            instance = new MySessionContext();
        }
        return instance;
    }

    /*
     * 给session监听器用的
     */
    public synchronized void addSession(HttpSession session) {
        if (session != null) {
            sessionMap.put(session.getId(), session);
        }
    }

    /*
     * 给session监听器用的
     */
    public synchronized void delSession(HttpSession session) {
        if (session != null) {
            sessionMap.remove(session.getId());
        }
    }

    /*
     * 给程序员用的，获得相应的session
     */
    public synchronized HttpSession getSession(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        return sessionMap.get(sessionId);
    }
}
