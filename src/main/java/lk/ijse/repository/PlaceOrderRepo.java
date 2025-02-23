package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.OrderDetail;
import lk.ijse.model.PlaceOrder;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderRepo {
    public static boolean placeOrder(PlaceOrder po) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isOrderSaved = OrderRepo.save(po.getOrder());
            if(isOrderSaved){
                boolean isQtyUpdated = ProductRepo.update(po.getOdtList());
                if (isQtyUpdated){
                    boolean isOrderDetailSaved = OrderDetailRepo.save(po.getOdtList());
                    if(isOrderDetailSaved){
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        }
       catch (Exception e){
            connection.rollback();
            return false;
       }finally {
            connection.setAutoCommit(true);
        }
    }
}
