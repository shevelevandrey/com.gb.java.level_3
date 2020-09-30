package com.gb.lesson_2.server;

import com.gb.lesson_2.server.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {
    public static int authorized = 1;
    public static int notAuthorized = 2;

    public AuthenticationService() {
    }

    public Client findClient(String login, String password) {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance();
            PreparedStatement statement = conn.prepareStatement("select * from clients where login = ? and password = ?");
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getInt("session_status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean isClientAuthorized(Client client) {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance();
            String sqlQuery = "select * " +
                                "from clients c " +
                                "join client_session_statuses css on css.id = c.session_status " +
                               "where c.login = ? " +
                                 "and c.session_status = ?";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, client.getLogin());
            statement.setInt(2, 1);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public void updateSessionStatus(Client client, int status) {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance();
            PreparedStatement statement = conn.prepareStatement("update clients set session_status = ? where login = ?");
            statement.setInt(1, status);
            statement.setString(2, client.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void renameClientLogin(Client client, String newLogin) {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance();
            PreparedStatement statement = conn.prepareStatement("update clients set login = ? where login = ?");
            statement.setString(1, newLogin);
            statement.setString(2, client.getLogin());
            statement.executeUpdate();

            client.login = newLogin;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Client {
        private int id;
        private String login;
        private String password;
        private String name;
        private int status;

        public Client(int id, String login, String password, String name, int status) {
            this.id = id;
            this.login = login;
            this.password = password;
            this.name = name;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public int getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    ", name='" + name + '\'' +
                    ", status=" + status +
                    '}';
        }
    }
}
