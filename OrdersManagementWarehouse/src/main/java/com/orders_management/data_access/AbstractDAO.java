package com.orders_management.data_access;

import com.orders_management.connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
public class AbstractDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public List<String> getAttributeNamesWithoutId() {
        List<String> attributeNames = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equalsIgnoreCase("id")) {
                attributeNames.add(fieldName);
            }
        }
        return attributeNames;
    }

    private String createSelectQuery(String field) {
        return "SELECT * FROM " + type.getSimpleName()+ " WHERE " + field + " =?";
    }
    private String createSelectQuery() {
        return "SELECT * FROM " + type.getSimpleName();
    }
    private String createInsertQuery() {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(type.getSimpleName()).append(" (");

        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equalsIgnoreCase("id")) {
                query.append(fieldName).append(", ");
            }
        }
        query.delete(query.length() - 2, query.length());
        query.append(") VALUES (");

        for (int i = 0; i < fields.length - 1; i++) {
            query.append("?, ");
        }
        query.delete(query.length() - 2, query.length());
        query.append(")");

        return query.toString();
    }

    private String createUpdateQuery() {
        StringBuilder query= new StringBuilder("UPDATE ");
        query.append(type.getSimpleName());
        query.append(" SET ");
        Field[] fields=type.getDeclaredFields();
        for(Field field: fields)
        {
            String fieldName=field.getName();
            if(!fieldName.equalsIgnoreCase("id")){
                query.append(fieldName).append(" = ?, ");
            }

        }
        query.delete(query.length()-2,query.length());
        query.append(" WHERE id = ?");
        return query.toString();
    }
    private String createDeleteQuery() {
        StringBuilder query= new StringBuilder("DELETE FROM ");
        query.append(type.getSimpleName());
        query.append(" WHERE id = ?");
        return query.toString();
    }

    public List<T> findAll() {
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        String query=createSelectQuery();
        try
        {
            connection=ConnectionFactory.getConnection();
            statement=connection.prepareStatement(query);
            resultSet=statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int parameterIndex = 1;
            for (Field field : type.getDeclaredFields()) {
                String fieldName = field.getName();
                if (!fieldName.equalsIgnoreCase("id")) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method getterMethod = propertyDescriptor.getReadMethod();
                    Object value = getterMethod.invoke(t);
                    statement.setObject(parameterIndex++, value);
                }
            }

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                PropertyDescriptor idPropertyDescriptor = new PropertyDescriptor("id", type);
                Method setterMethod = idPropertyDescriptor.getWriteMethod();
                setterMethod.invoke(t, resultSet.getInt(1));
            }

            return t;
        } catch (SQLException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


    public T update(T t) {
        Connection connection=null;
        PreparedStatement statement=null;
        int result=0;
        String query=createUpdateQuery();
        try
        {
            connection=ConnectionFactory.getConnection();
            statement= connection.prepareStatement(query);
            int parameterIndex=1;
            Object valueID = null;
            for(Field field : type.getDeclaredFields())
            {
                String fieldName = field.getName();
                if (!fieldName.equalsIgnoreCase("id")) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method getterMethod = propertyDescriptor.getReadMethod();
                    Object value = getterMethod.invoke(t);
                    statement.setObject(parameterIndex++, value);
                }
                else {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method getterMethod = propertyDescriptor.getReadMethod();
                    valueID = getterMethod.invoke(t);
                }
            }
            statement.setObject(parameterIndex,valueID);
            result = statement.executeUpdate();
            if(result>0)
            {
                return t;
            }

        } catch (SQLException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public boolean delete(T t) {
        Connection connection = null;
        int result = 0;
        PreparedStatement statement = null;
        String query = createDeleteQuery();

        try{
            connection=ConnectionFactory.getConnection();
            statement=connection.prepareStatement(query);
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[0].getName(), type);
            Method getterMethod = propertyDescriptor.getReadMethod();
            Object valueID = getterMethod.invoke(t);
            statement.setObject(1,valueID);
            result = statement.executeUpdate();
            if(result > 0)
            {
                return true;
            }
        } catch (SQLException  | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());

        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return false;
    }

}
