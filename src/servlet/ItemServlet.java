package servlet;

import com.mysql.cj.xdevapi.JsonArray;
import dto.ItemDto;
import org.apache.commons.dbcp2.BasicDataSource;
import util.CrudUtil;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/Item")
public class ItemServlet extends HttpServlet {


    public ItemServlet() {
        System.out.println("Item constructor");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Item init");
    }

    @Override
    public void destroy() {
        System.out.println("Item servlet destroy");
    }

    @Resource(name = "java:comp/env/db/pool")
    DataSource sd;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = sd.getConnection()) {

           /* ServletContext servletContext = getServletContext();
            int count  = (int) servletContext.getAttribute("count");
            System.out.println(count);

            count = count+1;

            servletContext.setAttribute("count",count);*/

            JsonArray jsonValues = new JsonArray();
            ArrayList<ItemDto> item = new ArrayList<>();

           /* ServletContext servletContext1 = getServletContext();
            BasicDataSource bds = (BasicDataSource) servletContext1.getAttribute("con");
            Connection connection = bds.getConnection();*/

            String sql = "SELECT * FROM Item";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();


            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                int qty = resultSet.getInt(3);
                double price = resultSet.getDouble(4);

                item.add(new ItemDto(id,name,qty,price));




            }
            /*String cusJson = "[";
            for (CustomerDto c: customer) {
                String id = c.getId();
                String name = c.getName();
                String address = c.getAddress();
                double salary = c.getSalary();
                String cusOb="{\"id\":\""+id+"\",\"name\":\""+name+"\",\"address\":\""+address+"\",\"salary\":"+salary+"},";
                cusJson+=cusOb;
            }
            String subString = cusJson.substring(0,cusJson.length()-1);
            subString+="]";

            resp.setContentType("application/json");

            resp.getWriter().write(subString);

*/
            JsonArrayBuilder array= Json.createArrayBuilder();
            JsonObjectBuilder object = Json.createObjectBuilder();
            JsonObjectBuilder object2 = Json.createObjectBuilder();

            for (ItemDto c:item) {
                object.add("id",c.getId());
                object.add("name",c.getName());
                object.add("qty",c.getQty());
                object.add("price",c.getPrice());

                array.add(object.build());
            }

            object2.add("state","done");
            object2.add("message","successes");
            object2.add("data",array.build());

            resp.getWriter().print(object2.build());

            /*req.setAttribute("cus",jsonValues);*/

            /* req.getRequestDispatcher("CustomerManage.html").forward(req,resp);*/


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

      /*  resp.addHeader("Access-Control-Allow-Origin","*");*/

        String id = req.getParameter("Code");
        String name = req.getParameter("Name");
        int qty = Integer.parseInt(req.getParameter("Qty"));
        double price = Double.parseDouble(req.getParameter("Price"));


     ItemDto c = new ItemDto(
                id, name, qty,
                price
        );

        JsonObjectBuilder object = Json.createObjectBuilder();


        try (Connection connection = sd.getConnection()){
            String sql3 = "INSERT INTO Item VALUES (?,?,?,?)";
            PreparedStatement stm3 = connection.prepareStatement(sql3);
            stm3.setString(1, id);
            stm3.setString(2, name);
            stm3.setDouble(3, price);
            stm3.setString(4, String.valueOf(qty));

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

        } catch ( SQLException e) {
           /* JsonObjectBuilder object = Json.createObjectBuilder();
            object.add("state", "failed");
            object.add("message", e.getMessage());
            resp.getWriter().print(object.build());
            resp.setStatus(500);*/

            e.printStackTrace();
        }


    }

   /* @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       *//* resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Headers","*");*//*
    }*/

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     /*   resp.addHeader("Access-Control-Allow-Origin","*");*/

        String id = req.getParameter("Code");

        try (Connection connection = sd.getConnection()){

            JsonObjectBuilder object = Json.createObjectBuilder();
            String sql ="Delete From Item Where code=?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1,id);

            int rowCount = stm.executeUpdate();

            if(rowCount>0){
                object.add("state","done");
                object.add("message","successes");
                resp.getWriter().print(object.build());
            }else {
                object.add("state","failed");
                object.add("message","Id not found");
                resp.getWriter().print(object.build());
                resp.setStatus(500);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /*  resp.addHeader("Access-Control-Allow-Origin","*");*/

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject item = reader.readObject();
        String code = item.getString("id");
        String description = item.getString("name");
        String qtyOnHand = item.getString("qty");
        String unitPrice = item.getString("price");

        JsonObjectBuilder object = Json.createObjectBuilder();




        try (Connection connection = sd.getConnection()){
            String sql = "Update Item Set description=?,qtyOnHand=?,unitPrice=? Where code=?";
            PreparedStatement stm = connection.prepareStatement(sql);

            stm.setString(4,code);
            stm.setString(1,description);
            stm.setDouble(2, Double.parseDouble(unitPrice));
            stm.setInt(3 ,Integer.parseInt(qtyOnHand));

            int rowCount = stm.executeUpdate();

            if(rowCount>0){
                object.add("state","done");
                object.add("message","successes");
                resp.getWriter().print(object.build());
            }else {
                object.add("state","failed");
                object.add("message","Id not found");
                resp.getWriter().print(object.build());
                resp.setStatus(500);

            }

        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }
}


