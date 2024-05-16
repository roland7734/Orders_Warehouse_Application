package com.orders_management.logic;

import com.orders_management.data_access.BillDAO;
import com.orders_management.models.Bill;

import java.util.List;

public class BillBLL {

    public static List<Bill> getAllBills()
    {
        BillDAO billDAO=new BillDAO();
        return BillDAO.getAllBills();
    }

}
