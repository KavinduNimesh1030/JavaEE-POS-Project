package servlet;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/purchase")
public class PlaceOrderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/db/pool")
    DataSource sd;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Connection connection = null;
        try{
            connection = sd.getConnection();
            System.out.println(connection);
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject requestJob = reader.readObject();

            String Oid = requestJob.getString("oid");
            String Date = requestJob.getString("date");
            String CusId = requestJob.getString("cusID");

            String sql = "INSERT INTO ORDERS VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,Oid);
            statement.setString(2,Date);
            statement.setString(3,CusId);

            if (!(statement.executeUpdate() > 0)) {
                connection.rollback();
                throw new RuntimeException("Can't Save The Order");
            }else{
                JsonArray jsonArray = requestJob.getJsonArray("orderDetails");
                for (JsonValue od :jsonArray){
                    String code = od.asJsonObject().getString("code");
                    String qty = od.asJsonObject().getString("qty");
                    String price = od.asJsonObject().getString("price");

                    String sql1 = "INSERT INTO OrderDetail VALUES (?,?,?,?)";
                    PreparedStatement statement1 = connection.prepareStatement(sql1);
                    statement1.setString(1,Oid);
                    statement1.setString(2,code);
                    statement1.setString(3,qty);
                    statement1.setString(4,price);

                    if (!(statement1.executeUpdate() > 0)) {
                        connection.rollback();
                        throw new RuntimeException("Can't Save The Order");
                    }
                }


                    connection.setAutoCommit(false);
                    connection.commit();
                    connection.close();

                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","done");
                responseObject.add("message","Successfully Purchased");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());



            }


        } catch (SQLException e) {
            e.printStackTrace();
            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
                System.out.println(connection);
            } catch (SQLException ex) {
                jsonObject.add("state","error");
                jsonObject.add("message",e.getMessage());
            }
            jsonObject.add("state","error");
            jsonObject.add("message",e.getMessage());
            resp.getWriter().print(jsonObject.build());
            resp.setStatus(500);
        }
    }
}
