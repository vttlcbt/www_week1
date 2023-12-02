package com.example.week01_lab_chaubichtuyen_20067001.repositories;

import com.example.week01_lab_chaubichtuyen_20067001.db.ConnectDB;
import com.example.week01_lab_chaubichtuyen_20067001.models.Account;
import com.example.week01_lab_chaubichtuyen_20067001.models.Role;
import com.example.week01_lab_chaubichtuyen_20067001.models.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository {
    public List<Account> getAll() throws SQLException, ClassNotFoundException {
        List<Account> listAccount = new ArrayList<>();
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "Select * from account";
            statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Status status = Status.fromCode(rs.getInt("status"));
                listAccount.add(
                        new Account(rs.getString("account_id"), rs.getString("full_name"), rs.getString("password"),
                                rs.getString("email"), rs.getString("phone"), status));
            }

            return listAccount;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean create(Account account) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "INSERT into account VALUES(?,?,?,?,?,?)";
            statement = con.prepareStatement(sql);
            statement.setString(1, account.getAccountId());
            statement.setString(2, account.getFullName());
            statement.setString(3, account.getPassword());
            statement.setString(4, account.getEmail());
            statement.setString(5, account.getPhone());
            statement.setInt(6, account.getStatus().getCode());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Account account) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE account SET full_name=?, email=?, password=?, phone=?, status=? WHERE account_id=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, account.getFullName());
            statement.setString(2, account.getEmail());
            statement.setString(3, account.getPassword());
            statement.setString(4, account.getPhone());
            statement.setInt(5, account.getStatus().getCode());
            statement.setString(6, account.getAccountId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean delete(String accountId) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM account WHERE account_id=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, accountId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Optional<Account> getById(String accountId) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM account WHERE account_id=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, accountId);
            ResultSet rs = statement.executeQuery();
            Account account = new Account();
            if (rs.next()) {
                account.setAccountId(rs.getString("account_id"));
                account.setFullName(rs.getString("full_name"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setPhone(rs.getString("phone"));
                account.setStatus(Status.fromCode(rs.getInt("status")));
            }
            return Optional.of(account);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Account> logon(String email, String password) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM account WHERE email=? AND password=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getString("account_id"));
                account.setFullName(rs.getString("full_name"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setPhone(rs.getString("phone"));
                account.setStatus(Status.fromCode(rs.getInt("status")));

                return Optional.of(account);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
