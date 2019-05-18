package com.empty.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionListener implements HttpSessionListener {
	private MySessionContext msc = MySessionContext.getInstance();

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		msc.addSession(session);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		msc.delSession(session);
	}
}
