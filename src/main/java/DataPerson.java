import java.sql.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataPerson {
    public static void main(String[] args) {

        try{
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:homework");

            createTablePerson(connection);
            insertTablePerson(connection);

            createTableDepartment(connection);
            insertTableDepartment(connection);

            addColumnAtPerson(connection);

            showPerson(connection);

            findDepartamentById(connection, 3);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public static void createTablePerson(Connection connection){
        try(Statement statement = connection.createStatement()){
            statement.execute("""
                                create table person(
                                    id bigint not null,
                                    name varchar(256),
                                    age integer
                                )
                            """);
        }catch (SQLException e){
            System.err.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }

    public static void insertTablePerson(Connection connection){
        try(Statement statement = connection.createStatement()){
            String human = "insert into person(id, name, age) values";
            human += "(1, 'Иван', 22),\n";
            human += "(2, 'Николай', 27),\n";
            human += "(3, 'Константин', 34),\n";
            human += "(4, 'Степан', 52),\n";
            human += "(5, 'Пётр', 47),\n";
            human += "(6, 'Василий', 25)\n";

            int insertCount = statement.executeUpdate(human);
            System.out.println("Внесены данные о " + insertCount + " сотрудниках");
        }catch (SQLException e){
            System.err.println("Ошибка заполнения таблицы: " + e.getMessage());
        }
    }


    public static void createTableDepartment(Connection connection){
        try(Statement statement = connection.createStatement()){
            statement.execute(""" 
                                       create table department
                                       (   
                                           id bigint primary key,
                                           name varchar(128) not null
                                        )
                                  """);
        }catch (SQLException e){
            System.err.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }

    public static void insertTableDepartment(Connection connection){
        try(Statement statement = connection.createStatement()){
            String depart = "insert into department(id, name) values";
            depart += "(1, 'Economica'),\n";
            depart += "(2, 'Pravo'),\n";
            depart += "(3, 'Health')\n";

            int insertCount = statement.executeUpdate(depart);
            System.out.println("Создано " + insertCount + " департамента");
        }catch (SQLException e){
            System.err.println("Ошибка заполнения таблицы: " + e.getMessage());
        }
    }

    public static void addColumnAtPerson(Connection connection){
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate("alter table person add department_id bigint");
            for (int i = 1; i <= 6; i++) {
                String result = String.valueOf(i);
                result = "update person set department_id = " + ThreadLocalRandom.current().nextInt(1, 4) + "where id = " + i;
                statement.executeUpdate(result);
            }
        }catch (SQLException e){
            System.err.println("Ошибка добавления столбца: " + e.getMessage());
        }
    }

    public static void showPerson(Connection connection){
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select * from person");
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                int department_id = resultSet.getInt("department_id");
                System.out.println("Найдена строка: id = " + id + ", имя = " + name +
                        ", возраст = " + age + ", department_id = " + department_id);
            }
        }catch(SQLException e){
            System.err.println("Ошибка вывода строк в терминал: " + e.getMessage());
        }

    }

    public static void findDepartamentById(Connection connection, int idPerson){
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select person.name, department.name from person " +
                    "join department on person.department_id = department.id where person.id = " + idPerson);
            while (resultSet.next()){
                String namePerson = resultSet.getString("person.name");
                String nameDepartment = resultSet.getString("department.name");
                System.out.println("По вашему запросу найдена строка: namePerson: " + namePerson + ", nameDepartment: " + nameDepartment);
            }
        }catch (SQLException e){
            System.err.println("Ошибка поиска департамента по id: " + e.getMessage());
        }

    }
}
