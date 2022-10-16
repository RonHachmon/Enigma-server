package chat.servlets.enigma;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;

public class MySessionAttributeListener implements HttpSessionAttributeListener {

    public MySessionAttributeListener() {
    }

    public void attributeAdded(HttpSessionBindingEvent sessionBindingEvent) {

        // Get the session
        HttpSession session = sessionBindingEvent.getSession();

        // Log some information
        System.out.println("[SessionAttr] " + new java.util.Date()
                + " Attribute added, session " + session + ": " + sessionBindingEvent.getName()
                + "=" + sessionBindingEvent.getValue());
    }

    public void attributeRemoved(HttpSessionBindingEvent sessionBindingEvent) {

        // Get the session
        HttpSession session = sessionBindingEvent.getSession();

        // Log some information
        System.out.println("[SessionAttr] " + new java.util.Date()
                + " Attribute removed, session " + session + ": "
                + sessionBindingEvent.getName());
    }

    public void attributeReplaced(HttpSessionBindingEvent sessionBindingEvent) {

        // Get the session
        HttpSession session = sessionBindingEvent.getSession();

        // Log some information
        System.out.println("[SessionAttr] " + new java.util.Date()
                + " Attribute replaced, session " + session + ": "
                + sessionBindingEvent.getName() + "=" + sessionBindingEvent.getValue());
    }
}