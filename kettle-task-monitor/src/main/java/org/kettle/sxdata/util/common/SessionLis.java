package org.kettle.sxdata.util.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Set;

/**
 * @description: Session监听
 * @author: ZX
 * @date: 2018/11/20 14:05
 */
public class SessionLis implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Set<String> set = StringDateUtil.allSession.keySet();
        for (String sessionId : set) {
            if (sessionId.equals(se.getSession().getId())) {
                StringDateUtil.allSession.remove(sessionId);
                break;
            }
        }
    }
}
