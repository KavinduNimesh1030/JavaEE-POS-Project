package servlet;

import com.mysql.cj.xdevapi.JsonArray;
import dto.CustomerDto;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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

@WebServlet(urlPatterns = "/DashBoard")
public class DashBoard extends HttpServlet {

    @Resource(name = "java:comp/env/db/pool")
    DataSource sd;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection connection = sd.getConnection()){
          /*  Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("db/pool");*/


            JsonArray jsonValues = new JsonArray();
            ArrayList<CustomerDto> board = new ArrayList<>();

            String sql = "SELECT COUNT(*) FROM item";
            String sql2 = "SELECT COUNT(*) FROM customer";
            String sql3 = "SELECT COUNT(*) FROM orders ";

            PreparedStatement stm = connection.prepareStatement(sql);
            PreparedStatement stm2 = connection.prepareStatement(sql2);
            PreparedStatement stm3 = connection.prepareStatement(sql3);

            ResultSet resultSet = stm.executeQuery();
            ResultSet resultSet2 = stm2.executeQuery();
            ResultSet resultSet3 = stm3.executeQuery();

            int itemCount =0;
            int customerCount =0;
            int orderCount =0;

            while (resultSet.next()){
                 itemCount = Integer.parseInt(resultSet.getString(1));
                System.out.println(itemCount);

             /*   board.add(new CustomerDto(id,name,address,salary));*/

            }
            while (resultSet2.next()){
                customerCount = Integer.parseInt(resultSet2.getString(1));
                System.out.println(customerCount);

            }
            while (resultSet3.next()){
                orderCount = Integer.parseInt(resultSet3.getString(1));
                System.out.println(orderCount);

            }

            JsonArrayBuilder array= Json.createArrayBuilder();
            JsonObjectBuilder object = Json.createObjectBuilder();
            JsonObjectBuilder object2 = Json.createObjectBuilder();


                object.add("itemCount",itemCount);
                object.add("cusCount",customerCount);
                object.add("orderCount",orderCount);

                array.add(object.build());

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
}
