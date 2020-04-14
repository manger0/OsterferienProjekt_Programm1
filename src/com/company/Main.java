package com.company;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            String url = "jdbc:mysql://localhost:3306/program1?user=root";
            connection = DriverManager.getConnection(url);

            // adding menu and ingredients to DB
            System.out.println("choose task, add menu, add city or end");
            System.out.println("insert:    menu  -  city  -  end");
            String task = scanner.nextLine();
            while (!task.equalsIgnoreCase("end")) {
                if (task.equalsIgnoreCase("menu")) addMenu(connection);
                if (task.equalsIgnoreCase("city")) addCityPrice(connection);
                System.out.println("choose task, add Menu  -  add city  -  end");
                System.out.println("insert:    menu  -  city  -  end");
                task = scanner.nextLine();
            }
        } catch (SQLException e) {
            throw new Error("connection problem", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    static void addMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("insert menu name:");
        String menuName = scanner.nextLine();
        System.out.println("insert menu price");
        double price = scanner.nextDouble();
        String[] ingredientsOfMenu = buildIngredientArray();
        ingredientToDB(ingredientsOfMenu, connection);
        final String menuINSERT = "INSERT INTO `menu` " +
                "(`menu_name`, `price`, `ingredient1`, `ingredient2`, `ingredient3`, `ingredient4`, `ingredient5`," +
                "`ingredient6`, `ingredient7`, `ingredient8`, `ingredient9`, `ingredient10`)" +
                "VALUES" + "('" + menuName + "', '" + price + "'," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[0] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[1] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[2] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[3] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[4] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[5] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[6] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[7] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[8] + "')," +
                "(SELECT ingredient_id from ingredients WHERE ingredient_name = '" + ingredientsOfMenu[9] + "'))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(menuINSERT)) {
            preparedStatement.executeUpdate();
            System.out.println("menu added");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static String[] buildIngredientArray() {
        Scanner scanner = new Scanner(System.in);
        String[] ingredientsOfMenu = new String[10];
        Arrays.fill(ingredientsOfMenu, null);
        String ingredient = "";
        int count = 0;
        while (!ingredient.equalsIgnoreCase("exit")) {
            System.out.println("insert ingredient or insert exit");
            ingredient = scanner.nextLine();
            if (!ingredient.equalsIgnoreCase("exit")) {
                ingredientsOfMenu[count] = ingredient;
                count++;
            }
        }
        return ingredientsOfMenu;
    }

    public static void ingredientToDB(String[] ingredientsOfMenu, Connection connection) {
        final String ingredientINSERT = "INSERT INTO ingredients (ingredient_name)" + "Values (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(ingredientINSERT)) {
            for (String ingredient : ingredientsOfMenu) {
                if (ingredient != null || ingredient.equals("")) {
                    preparedStatement.setString(1, ingredient);
                    preparedStatement.executeUpdate();
                } else break;
            }
        } catch (SQLException | NullPointerException ignored) {
        }
    }

    public static void addCityPrice(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("insert city");
        String city = scanner.nextLine();
        System.out.println("insert delivery price for city " + city);
        double deliveryPrice = scanner.nextDouble();
        final String SQL_INSERT = "INSERT INTO city_price (city_name, delivery_price)" +
                "Values (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, city);
            preparedStatement.setDouble(2, deliveryPrice);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("delivery price problem");
        }
    }
}
