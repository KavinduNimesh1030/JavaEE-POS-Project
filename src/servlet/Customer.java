package servlet;

import com.mysql.cj.xdevapi.JsonArray;
import dto.CustomerDto;
import org.apache.commons.dbcp2.BasicDataSource;
import util.CrudUtil;

import javax.annotation.Resource;
import javax.json.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/SaveCustomer")
public class Customer extends HttpServlet {

    public Customer() {
        System.out.println("customer constructor");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Customer init");
    }

    @Override
    public void destroy() {
        System.out.println("customer servlet destroy");
    }


    @Resource(name = "java:comp/env/db/pool")
    DataSource sd;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {



        try (Connection connection = sd.getConnection()){
          /*  Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("db/pool");*/


            JsonArray jsonValues = new JsonArray();
            ArrayList<CustomerDto> customer = new ArrayList<>();

            String sql = "SELECT * FROM customer";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                double salary = resultSet.getDouble(4);

                customer.add(new CustomerDto(id,name,address,salary));

            }

            JsonArrayBuilder array= Json.createArrayBuilder();
            JsonObjectBuilder object = Json.createObjectBuilder();
            JsonObjectBuilder object2 = Json.createObjectBuilder();

            for (CustomerDto c:customer) {
                object.add("id",c.getId());
                object.add("name",c.getName());
                object.add("address",c.getAddress());
                object.add("salary",c.getSalary());

                array.add(object.build());
            }
            object2.add("state","done");
            object2.add("message","successes");
            object2.add("data",array.build());

           resp.getWriter().print(object2.build());
       /*   sd.getConnection().close();*/

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            JsonObjectBuilder object = Json.createObjectBuilder();
            object.add("state", "failed");
            object.add("message", throwables.getMessage());
            resp.getWriter().print(object.build());
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String id = req.getParameter("ID");
        String name = req.getParameter("Name");
        String address = req.getParameter("Address");
        String salary = req.getParameter("Salary");
        String option = req.getParameter("option");

            CustomerDto c = new CustomerDto(
                    id, name, address,
                    Double.parseDouble(salary)
            );

        try (Connection connection = sd.getConnection()){

                String sql2 = "INSERT INTO customer VALUES (?,?,?,?)";
                PreparedStatement stm2 = connection.prepareStatement(sql2);
                stm2.setString(1, id);
                stm2.setString(2, name);
                stm2.setString(3, address);
                stm2.setDouble(4, Double.parseDouble(salary));


                int rowCount = stm2.executeUpdate();
                JsonObjectBuilder object = Json.createObjectBuilder();
                if (rowCount>0){
                    object.add("state","done");
                    object.add("message","successful");
                    resp.getWriter().print(object.build());

                }

            } catch (SQLException e) {
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("state", "failed");
                object.add("message", e.getMessage());
                resp.getWriter().print(object.build());
                resp.setStatus(500);

                e.printStackTrace();
            }



    }



    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String id = req.getParameter("ID");

        try(Connection connection = sd.getConnection()) {
            String sql1 = "DELETE FROM customer WHERE id=?";
            PreparedStatement stm1 = connection.prepareStatement(sql1);
            stm1.setString(1, id);

            int rowCount = stm1.executeUpdate();

            JsonObjectBuilder object = Json.createObjectBuilder();

            if (rowCount>0){
                object.add("state","done");
                object.add("message","successfully deleted");
                resp.getWriter().print(object.build());

            }else{
                object.add("state","failed");
                object.add("message","Id not found");
                resp.getWriter().print(object.build());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        String salary = customer.getString("salary");
        JsonObjectBuilder object = Json.createObjectBuilder();



        try(Connection connection = sd.getConnection()) {
            String sql3 = "UPDATE customer SET name=?,address=?,salary=? WHERE id=?";
            PreparedStatement stm3 = connection.prepareStatement(sql3);
            stm3.setString(1, name);
            stm3.setString(2, address);
            stm3.setDouble(3, Double.parseDouble(salary));
            stm3.setString(4, id);

            int rowCount = stm3.executeUpdate();

            if (rowCount>0){
                object.add("state","done");
                object.add("message","successful");
                resp.getWriter().print(object.build());

            }else{
                object.add("state","failed");
                object.add("message","Id not found");
                resp.getWriter().print(object.build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

