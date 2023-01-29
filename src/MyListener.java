import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet context Initialize");
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/thogakade");
        bds.setUsername("root");
        bds.setPassword("root1234");

        bds.setMaxTotal(2);
        bds.setInitialSize(2);

        ServletContext servletContext1 = servletContextEvent.getServletContext();
        servletContext1.setAttribute("con",bds);


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet context Destroyed");
    }
}
