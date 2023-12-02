package com.example.week01_lab_chaubichtuyen_20067001.controllers;

import com.example.week01_lab_chaubichtuyen_20067001.models.Account;
import com.example.week01_lab_chaubichtuyen_20067001.models.Logs;
import com.example.week01_lab_chaubichtuyen_20067001.models.Role;
import com.example.week01_lab_chaubichtuyen_20067001.models.Status;
import com.example.week01_lab_chaubichtuyen_20067001.repositories.AccountRepository;
import com.example.week01_lab_chaubichtuyen_20067001.repositories.GrantAccessRepository;
import com.example.week01_lab_chaubichtuyen_20067001.repositories.LogRepository;
import com.example.week01_lab_chaubichtuyen_20067001.repositories.RoleRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "controlServlet", value = "/control-servlet")
public class ControlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        RoleRepository roleRepository = new RoleRepository();
        AccountRepository accountRepository = new AccountRepository();
        switch (action) {
            case "listRole":
                try {
                    List<Role> listRole = roleRepository.getAll();
                    req.setAttribute("listRole", listRole);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/role/roles.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "editRole":
                try {
                    Role role = roleRepository.getById(req.getParameter("id"));
                    req.setAttribute("role", role);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/role/edit_role.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "deleteRole":
                try {
                    roleRepository.delete(String.valueOf(req.getParameter("id")));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "addRole": {
                RequestDispatcher dispatcher = req.getRequestDispatcher("/role/add_role.jsp");
                dispatcher.forward(req, resp);
                break;
            }
            case "dashboard":

                try {

                    Cookie[] cookies = req.getCookies();
                    String accountId = "";
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("account_id")) {
                            accountId = cookie.getValue();
                        }
                    }

                    List<Role> listRoleByAccount = roleRepository.getRoleByAccount(accountId);
                    req.setAttribute("listRoleByAccount", listRoleByAccount);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("dashboard.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "logout": {

                Cookie[] cookies = req.getCookies();

                String accountId = "";
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("account_id")) {
                        accountId = cookie.getValue();
                    }
                }

                Logs log = new Logs();
                log.setAccount(new Account(accountId));
                log.setLogoutTime(new Timestamp(System.currentTimeMillis()));
                log.setNotes("logout");
                try {
                    new LogRepository().update(log);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                }

                RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
                dispatcher.forward(req, resp);
                break;
            }
            case "listAccount":
                try {
                    List<Account> listAccount = accountRepository.getAll();
                    List<Role> listRole = roleRepository.getAll();
                    req.setAttribute("listAccount", listAccount);
                    req.setAttribute("listRole", listRole);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/account/accounts.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "editAccount":
                try {
                    Optional<Account> account = accountRepository.getById(req.getParameter("id"));
                    req.setAttribute("account", account.get());
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/account/edit_account.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "deleteAccount":
                try {
                    accountRepository.delete(String.valueOf(req.getParameter("id")));

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "addAccount": {
                RequestDispatcher dispatcher = req.getRequestDispatcher("/account/add_account.jsp");
                dispatcher.forward(req, resp);
                break;
            }
            case "listLog":
                try {
                    List<Logs> listLog = new LogRepository().getAll();
                    req.setAttribute("listLog", listLog);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/log/logs.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        AccountRepository accountRepository = new AccountRepository();
        RoleRepository roleRepository = new RoleRepository();
        GrantAccessRepository grantAccessRepository = new GrantAccessRepository();
        LogRepository logRepository = new LogRepository();
        Role role = toRole(req);

        switch (action) {
            case "logon":
                try {
                    Optional<Account> account = accountRepository.logon(req.getParameter("email"),
                            req.getParameter("password"));

                    if (account.isPresent()) {
                        Logs log = new Logs();
                        log.setAccount(account.get());
                        log.setLoginTime(new Timestamp(System.currentTimeMillis()));
                        log.setNotes("login");
                        logRepository.create(log);
                        Cookie account_id = new Cookie("account_id", account.get().getAccountId());
                        Cookie full_name = new Cookie("full_name", account.get().getFullName());
                        Cookie email = new Cookie("email", account.get().getEmail());
                        Cookie phone = new Cookie("phone", account.get().getPhone());
                        Cookie status = new Cookie("status", account.get().getStatus().toString());
                        resp.addCookie(account_id);
                        resp.addCookie(full_name);
                        resp.addCookie(email);
                        resp.addCookie(phone);
                        resp.addCookie(status);

                        req.setAttribute("account", account.get());
                        RequestDispatcher dispatcher = req.getRequestDispatcher("dashboard.jsp");
                        dispatcher.forward(req, resp);
                    }
                } catch (SQLException | ClassNotFoundException | ServletException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "register":
            case "addAccount": {
                String uuid = UUID.randomUUID().toString();
                Account account = toAccount(req);
                account.setAccountId(uuid);
                if (req.getParameter("status").equals("1")) {
                    account.setStatus(Status.ACTIVE);
                } else if (req.getParameter("status").equals("0")) {
                    account.setStatus(Status.DEACTIVATE);
                } else {
                    account.setStatus(Status.DELETE);
                }
                try {
                    accountRepository.create(account);
                    if (action.equals("register")) {
                        resp.sendRedirect("index.jsp");
                    } else {
                        resp.sendRedirect("control-servlet?action=listAccount");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                break;
            }
            case "editRole": {
                if (req.getParameter("status").equals("1")) {
                    role.setStatus(Status.ACTIVE);
                } else if (req.getParameter("status").equals("0")) {
                    role.setStatus(Status.DEACTIVATE);
                } else {
                    role.setStatus(Status.DELETE);
                }
                try {
                    boolean rs = roleRepository.update(role);
                    if (rs) {
                        resp.sendRedirect("control-servlet?action=listRole");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "addRole": {
                String uuid = UUID.randomUUID().toString();
                role.setRoleId(uuid);
                if (req.getParameter("status").equals("1")) {
                    role.setStatus(Status.ACTIVE);
                } else if (req.getParameter("status").equals("0")) {
                    role.setStatus(Status.DEACTIVATE);
                } else {
                    role.setStatus(Status.DELETE);
                }
                try {
                    boolean rs = roleRepository.add(role);
                    if (rs) {
                        resp.sendRedirect("control-servlet?action=listRole");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "editAccount": {
                Account account = toAccount(req);
                if (req.getParameter("status").equals("1")) {
                    account.setStatus(Status.ACTIVE);
                } else if (req.getParameter("status").equals("0")) {
                    account.setStatus(Status.DEACTIVATE);
                } else {
                    account.setStatus(Status.DELETE);
                }
                try {
                    boolean rs = accountRepository.update(account);
                    if (rs) {
                        resp.sendRedirect("control-servlet?action=listAccount");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "grantPermission":
                try {
                    grantAccessRepository.grantPermission(req.getParameter("accountId"),
                            req.getParameter("roleIds"), req.getParameter("grantType"), req.getParameter("note"));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private Account toAccount(HttpServletRequest req) {
        Account account = new Account();
        account.setAccountId(req.getParameter("account_id"));
        account.setPassword(req.getParameter("password"));
        account.setFullName(req.getParameter("full_name"));
        account.setEmail(req.getParameter("email"));
        account.setPhone(req.getParameter("phone"));
        return account;
    }

    private Role toRole(HttpServletRequest req) {
        Role role = new Role();
        role.setRoleId(req.getParameter("role_id"));
        role.setRoleName(req.getParameter("role_name"));
        role.setDescription(req.getParameter("description"));
        return role;
    }
}
