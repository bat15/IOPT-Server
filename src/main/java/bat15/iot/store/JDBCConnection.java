/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Павел
 */
public class JDBCConnection {
    
    
    public enum RequestType { JARUS, DPI };
//    private final Map<String, HTableInterface> hbaseTables = new HashMap<>();
    private boolean isDpi;
    
//    String dbHost = "localhost";
//    String dbPort = "5432";
//    String dbName = "wsobjects";
//    String dbUser = "postgres";
//    String dbPassword = "1234";

    
    Connection jdbcConn = null;

    private JDBCConnection(String host, String port, String dbName, String user, String password) throws SQLException{

        String url = "";
        
        
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер подключен");            
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("!!!CLASS_NOT_FOUND_ERROR!!!");
            ex.printStackTrace();
        }   
        
        
        //"jdbc:postgresql://localhost:5432/wsobjects?user=postgres&password=1234&ssl=false";
        url = "jdbc:postgresql://"+ host +":"+port+"/"+dbName+"?user="+user+"&password="+password+"&ssl=false";


        jdbcConn = DriverManager.getConnection(url);

    }
    
    public static JDBCConnection createConnection (String host, String port, String dbName, String user, String password)throws SQLException {
        return new JDBCConnection(host, port, dbName, user, password);
    }
    
    
//    public void disconnect(){
//        closeTables();
//        closeConnection();
//    }  
    public void disconnect()
    {
        if (jdbcConn != null)
        {
            try { jdbcConn.close(); } catch (SQLException e) {}
        }      
        
        System.out.println("JDBC Connection closed");
    }

    
    public ArrayList<Object> getResult(String k, long startDate, long endData)
    {       
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver connected");            
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("!!!CLASS_NOT_FOUND_ERROR!!!");
            ex.printStackTrace();
        }   
        
        Statement  stmt = null;
        ResultSet rs = null;
        
        try
        {  
            if(jdbcConn == null) System.out.println("!!!!!!!!!!! jdbcConn Nullpointer ERROR !!!!!");
            
            stmt = jdbcConn.createStatement();
            String sql;
            sql = " SELECT * FROM table " + 
                  " WHERE id='" + k + 
                  "' AND dat>'" + startDate +
                  "' AND dat<'" + endData + "';";
            
            System.out.println("SQL_Str: " + sql);
            
            rs = stmt.executeQuery(sql);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("!!!!!!!!!!! SQL QUERY ERROR !!!!!");
        }
        
         return new ArrayList();   
    }    
               
    
    public ArrayList<String> showTables()
    {
        ArrayList<String> tables = new ArrayList();
        
        
        String query = "select table_name FROM information_schema.tables " 
            + " WHERE table_schema = 'public'";
        

        int resultSize = 0;
        
        try (Statement results = jdbcConn.createStatement()) {
          try (ResultSet rs = results.executeQuery(query)) {
              
            while (rs.next()) {
                resultSize++;
                
                System.out.println("table_name: " + rs.getObject("table_name"));
                
                try{
                    tables.add(rs.getString("table_name"));
                }catch(Exception ex){
                    System.out.println(rs.getObject("tables.add Exception"));
                }
            }
          }
          catch(SQLException e){
              System.out.println("SQLException: " + e.getMessage());
          }
        }
        catch(Exception ex){System.out.println("Exception: " + ex.getMessage());}
        
        System.out.println("resultSize: " + resultSize);
        
        return tables;
    }
    
    public String selectValue(String tableName, String searchKey, String whereKey, String whereValue)
    {
        
        String resultValue = null;
        
        String selectQuery = "SELECT " + searchKey + "";
        String fromTablesQuery = " FROM \"" + tableName + "\"";
        
        String whereConditionsQuery = " WHERE \"" + whereKey + "\"='" + whereValue + "'";

        selectQuery += fromTablesQuery + whereConditionsQuery;

                
        System.out.println("SELECT Query: " + selectQuery);
                
        try (Statement results = jdbcConn.createStatement()) {   
            try(ResultSet rs = results.executeQuery(selectQuery))
            {
                rs.next();
                resultValue =  rs.getString(searchKey.replace("\"", ""));
            }catch(Exception ex){
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            return resultValue;
        }

        
        return resultValue;
    }
    
    public HashMap<String, String> selectValue(ArrayList<String> searchKeys, ArrayList<String> tables, Map<String, String> whereFields)
    {
        
        String searchKeysQuery = "SELECT ";
        
        int i = 0;
        
        for(String searchKey:searchKeys)
        {
            if(i < searchKeys.size() - 1)searchKeysQuery += "\"" + searchKey + "\", ";
            else searchKeysQuery += "\"" + searchKey + "\" ";
            i++;
        }  

        
        
        String fromTablesQuery = " FROM ";
        
        i = 0;
        
        for(String tableName:tables)
        {
            if(i < tables.size() - 1)fromTablesQuery += "\"" + tableName + "\", ";
            else fromTablesQuery += "\"" + tableName + "\" ";
            i++;
        }        
        
        
        i = 0;
        
        String whereFieldsQuery = " WHERE ";
        
        for(String fieldName:whereFields.keySet())
        {
            if(i < whereFields.size() - 1)whereFieldsQuery += "\"" + fieldName + "\"='" + whereFields.get(fieldName) + "' AND ";
            else whereFieldsQuery += "\"" + fieldName + "\"='" + whereFields.get(fieldName) + "' ";
            i++;
        }
        
        
        
        ResultSet rs = null;
        
        
        HashMap<String, String> resultHashMap = new HashMap();
        
        String selectQuery = searchKeysQuery;
        selectQuery += fromTablesQuery;
        selectQuery += whereFieldsQuery;

        
        
        try (Statement results = jdbcConn.createStatement()) {       
            rs = results.executeQuery(selectQuery);
            rs.next();
            
            for(String searchKey:searchKeys)
            {
                resultHashMap.put(searchKey, rs.getString(searchKey));
            }             
            
            
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            return null;
        }
        
        
        return resultHashMap;
    }
    
    public String selectValue(String tableName, String searchKey, Map<String, String> whereFields) 
    {
        
        ResultSet rs = null;
        String resultValue = null;
        String selectQuery = "SELECT " + searchKey + "";
        String fromTablesQuery = " FROM \"" + tableName + "\" ";
 
        int i = 0;
        
        String whereFieldsQuery = " WHERE ";
        
        for(String fieldName:whereFields.keySet())
        {
            if(i < whereFields.size() - 1)whereFieldsQuery += "\"" + fieldName + "\"='" + whereFields.get(fieldName) + "' AND ";
            else whereFieldsQuery += "\"" + fieldName + "\"='" + whereFields.get(fieldName) + "' ";
            i++;
        }
        
        
        selectQuery += fromTablesQuery + whereFieldsQuery;
        
        try (Statement results = jdbcConn.createStatement()) {       
            rs = results.executeQuery(selectQuery);
            rs.next();
            resultValue =  rs.getString(searchKey.replace("\"", ""));
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            return resultValue;
        }
        
        
        return resultValue;
    }    
    
    public String selectValue(ArrayList<String> tables, String searchKey, ArrayList<String> whereConditions) 
    {
        
        ResultSet rs = null;
        String resultValue = null;
        String selectQuery = "SELECT " + searchKey + "";
        
        //+ "\" FROM \"" + tableName + "\" ";
        
 
        int i = 0;
        
        
        String fromTablesQuery = " FROM ";
        

        for(String tableName:tables)
        {
            if(i < tables.size() - 1)fromTablesQuery += "\"" + tableName + "\", ";
            else fromTablesQuery += "\"" + tableName + "\" ";
            i++;
        }             
        
        selectQuery += fromTablesQuery;
        
        i = 0;
        String whereConditionsQuery = " WHERE ";
        
        for(String whereStr:whereConditions)
        {
            if(i < whereConditions.size() - 1)whereConditionsQuery += whereStr + " AND ";
            else whereConditionsQuery += whereStr + " ";
            i++;
        }
        
        
        selectQuery += whereConditionsQuery;
        
        System.out.println("SELECT Query: " + selectQuery);
        
        try (Statement results = jdbcConn.createStatement()) {       
            rs = results.executeQuery(selectQuery);
            rs.next();
            resultValue =  rs.getString(searchKey.replace("\"", ""));
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            return resultValue;
        }
        
        
        return resultValue;
    }
    
    
    public String selectValue(ArrayList<String> tables, String searchKeyPrefix, String searchKey, ArrayList<String> whereConditions) 
    {
        
        ResultSet rs = null;
        String resultValue = null;
        String selectQuery = "SELECT " + searchKeyPrefix + "." + searchKey + "";
        
        //+ "\" FROM \"" + tableName + "\" ";
        
 
        int i = 0;
        
        
        String fromTablesQuery = " FROM ";
        

        for(String tableName:tables)
        {
            if(i < tables.size() - 1)fromTablesQuery += "\"" + tableName + "\", ";
            else fromTablesQuery += "\"" + tableName + "\" ";
            i++;
        }             
        
        selectQuery += fromTablesQuery;
        
        i = 0;
        String whereConditionsQuery = " WHERE ";
        
        for(String whereStr:whereConditions)
        {
            if(i < whereConditions.size() - 1)whereConditionsQuery += whereStr + " AND ";
            else whereConditionsQuery += whereStr + " ";
            i++;
        }
        
        
        selectQuery += whereConditionsQuery;
        
        System.out.println("SELECT Query: " + selectQuery);
        
        try (Statement results = jdbcConn.createStatement()) {       
            rs = results.executeQuery(selectQuery);
            rs.next();
            resultValue =  rs.getString(searchKey.replace("\"", ""));
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            return resultValue;
        }
        
        
        return resultValue;
    } 
    
    
    public ArrayList<String> selectList(ArrayList<String> tables, String searchKey, ArrayList<String> whereConditions) 
    {
        
        ArrayList<String> resultList = new ArrayList();
        String selectQuery = "SELECT " + searchKey + "";
        
        //+ "\" FROM \"" + tableName + "\" ";
        
 
        int i = 0;
        
        
        String fromTablesQuery = " FROM ";
        

        for(String tableName:tables)
        {
            if(i < tables.size() - 1)fromTablesQuery += "\"" + tableName + "\",";
            else fromTablesQuery += "\"" + tableName + "\"";
            i++;
        }             
        
        selectQuery += fromTablesQuery;
        
        i = 0;
        String whereConditionsQuery = " WHERE ";
        
        for(String whereStr:whereConditions)
        {
            if(i < whereConditions.size() - 1)whereConditionsQuery += whereStr + " AND ";
            else whereConditionsQuery += whereStr + ";";
            i++;
        }
        
        
        selectQuery += whereConditionsQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        int resultSize = 0;
        
        try (Statement results = jdbcConn.createStatement()) {
          try (ResultSet rs = results.executeQuery(selectQuery)) {
              
            while (rs.next()) {
                resultSize++;

                
                try{
                    resultList.add(rs.getString(searchKey.replace("\"", "")));
                }catch(Exception ex){
                    System.out.println("resultList.add Exception");
                }
            }
          }
          catch(SQLException e){
              System.out.println("SQLException: " + e.getMessage());
          }
        }
        catch(Exception ex){System.out.println("Exception: " + ex.getMessage());}
        
        
        return resultList;
    }       
    

    
    public HashMap<String, String> selectHash(ArrayList<String> tables, ArrayList<String> searchKeys, ArrayList<String> whereConditions) 
    {
        
        HashMap<String, String> resultHashMap = new HashMap();
        
        String selectQuery = "SELECT ";
        
        int i = 0;
        int resultSize = 0;
        
        for(String searchKey:searchKeys)
        {
            if(i < searchKeys.size() - 1)selectQuery += "" + searchKey + ",";
            else selectQuery += "" + searchKey + "";
            i++;
            resultSize++;
        }  

        
        //+ "\" FROM \"" + tableName + "\" ";
        
 
         i = 0;
        
        
         String fromTablesQuery = " FROM ";
        

        for(String tableName:tables)
        {
            if(i < tables.size() - 1)fromTablesQuery += "\"" + tableName + "\",";
            else fromTablesQuery += "\"" + tableName + "\"";
            i++;
        }             
        
        selectQuery += fromTablesQuery;
        
        
        i = 0;
        String whereConditionsQuery = " WHERE ";
        
        for(String whereStr:whereConditions)
        {
            if(i < whereConditions.size() - 1)whereConditionsQuery += whereStr + " AND ";
            else whereConditionsQuery += whereStr + ";";
            i++;
        }
        
        
        selectQuery += whereConditionsQuery;
        
        
        System.out.println("selectQuery: " + selectQuery);
        
        
        
        try (Statement results = jdbcConn.createStatement()) {
          try (ResultSet rs = results.executeQuery(selectQuery)) {
              
             
            if(rs.next())
            {
                for(String key:searchKeys)
                {
                    key = key.replace("\"", "");
                    String value = rs.getString(key);

                    System.out.println("key: " + key + " value: " + value);

                    try{
                        resultHashMap.put(key, value);
                    }catch(Exception ex){
                        System.out.println("resultList.add Exception");
                        System.out.println("Exception with key: " + key + " value: " + value);
                    }

                }
            }
            else System.out.println("EMPTY RESULT!");
          }
          catch(SQLException e){
              System.out.println("SQLException: " + e.getMessage());
          }
        }
        catch(Exception ex){System.out.println("Exception: " + ex.getMessage());}
        
        
        return resultHashMap;
    }       
    
    
    
    
    public HashMap<String, String> selectScriptValueByPath(HashMap<String, String> pathUnits, String userId)
    {
        String selectQuery = "SELECT \"script\".\"value\",\"script\".\"name\"";
        
        String fromQuery = " FROM \"model\",\"object\",\"property\",\"script\"";
        
        String whereQuery = " WHERE \"id_user\"=" + userId + " AND " +
                "\"model\".\"path_unit\"='" + pathUnits.get("model") + "' AND " +
                "\"object\".\"path_unit\"='" + pathUnits.get("object") + "' AND " +
                "\"property\".\"path_unit\"='" + pathUnits.get("property") + "' AND " +
                "\"script\".\"path_unit\"='" + pathUnits.get("script") + "';";
        
        selectQuery += fromQuery + whereQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        HashMap<String, String> result = new HashMap();
        
        try (Statement results = jdbcConn.createStatement()) {
            try (ResultSet rs = results.executeQuery(selectQuery)) {
                
                rs.next();
                result.put("name", rs.getString("name"));
                result.put("value", rs.getString("value"));
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
        }     
        
        return result;
    }
    
    public HashMap<String, String> selectPropertyValueByPath(HashMap<String, String> pathUnits, String userId)
    {
        String selectQuery = "SELECT \"property\".\"value\",\"property\".\"name\",\"property\".\"type\"";
        
        String fromQuery = " FROM \"model\",\"object\",\"property\"";
        
        String whereQuery = " WHERE \"id_user\"=" + userId + " AND " +
                "\"model\".\"path_unit\"='" + pathUnits.get("model") + "' AND " +
                "\"object\".\"path_unit\"='" + pathUnits.get("object") + "' AND " +
                "\"property\".\"path_unit\"='" + pathUnits.get("property") + "';";
        
        selectQuery += fromQuery + whereQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        HashMap<String, String> result = new HashMap();
        
        try (Statement results = jdbcConn.createStatement()) {
            try (ResultSet rs = results.executeQuery(selectQuery)) {
                
                rs.next();
                result.put("name", rs.getString("name"));
                result.put("value", rs.getString("value"));
                result.put("type", rs.getString("type"));
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
        }     
        
        return result;
    }
    
    public HashMap<String, String> selectObjectValueByPath(HashMap<String, String> pathUnits, String userId)
    {
        String selectQuery = "SELECT \"object\".\"value\",\"object\".\"name\"";
        
        String fromQuery = " FROM \"model\",\"object\"";
        
        String whereQuery = " WHERE \"id_user\"=" + userId + " AND " +
                "\"model\".\"path_unit\"='" + pathUnits.get("model") + "' AND " +
                "\"object\".\"path_unit\"='" + pathUnits.get("object") + 
                "';";
        
        selectQuery += fromQuery + whereQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        HashMap<String, String> result = new HashMap();
        
        try (Statement results = jdbcConn.createStatement()) {
            try (ResultSet rs = results.executeQuery(selectQuery)) {
                
                rs.next();
                result.put("name", rs.getString("name"));
       
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
        }     
        
        return result;
    }
    
    
    public boolean updateScriptValueByPath(HashMap<String, String> pathUnits, String userId, String value)
    {
        String selectQuery = "SELECT \"script\".\"id\"";
        
        String fromQuery = " FROM \"model\",\"object\",\"property\",\"script\"";
        
        String whereQuery = " WHERE \"id_user\"=" + userId + " AND " +
                "\"model\".\"path_unit\"='" + pathUnits.get("model") + "' AND " +
                "\"object\".\"path_unit\"='" + pathUnits.get("object") + "' AND " +
                "\"property\".\"path_unit\"='" + pathUnits.get("property") + "' AND " +
                "\"script\".\"path_unit\"='" + pathUnits.get("script") + "';";
        
        selectQuery += fromQuery + whereQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        HashMap<String, String> result = new HashMap();
        
        try (Statement results = jdbcConn.createStatement()) {
            try (ResultSet rs = results.executeQuery(selectQuery)) {
                
                if(rs.next()){//not empty - do update
                    
                    String id = rs.getString("id");
                    
                    String updateQuery = "UPDATE \"script\" SET \"value\"='" + value + "'";
                    updateQuery += " WHERE \"id\"=" + id + ";";
                    
                    System.out.println("updateQuery: " + updateQuery);
                    
                    results.executeUpdate(updateQuery);
                    
                    return true;
                }else{
                    
                    return false;
                }
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            
            return false;
        }     
    }  
            
    public boolean updatePropertyValueByPath(HashMap<String, String> pathUnits, String userId, String value)
    {
        String selectQuery = "SELECT \"property\".\"id\"";
        
        String fromQuery = " FROM \"model\",\"object\",\"property\"";
        
        String whereQuery = " WHERE \"id_user\"=" + userId + " AND " +
                "\"model\".\"path_unit\"='" + pathUnits.get("model") + "' AND " +
                "\"object\".\"path_unit\"='" + pathUnits.get("object") + "' AND " +
                "\"property\".\"path_unit\"='" + pathUnits.get("property") + "';";
        
        selectQuery += fromQuery + whereQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        HashMap<String, String> result = new HashMap();
        
        try (Statement results = jdbcConn.createStatement()) {
            try (ResultSet rs = results.executeQuery(selectQuery)) {
                
                if(rs.next()){//not empty - do update
                    
                    String id = rs.getString("id");
                    
                    String updateQuery = "UPDATE \"property\" SET \"value\"='" + value + "'";
                    updateQuery += " WHERE \"id\"=" + id;
                    System.out.println("updateQuery: " + updateQuery);
                    
                    results.executeUpdate(updateQuery);
                    
                    return true;
                }else{
                    
                    return false;
                }
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
            
            return false;
        }     
        

    }
    
    
    public HashMap<String, String> selectModelValueByPath(HashMap<String, String> pathUnits, String userId)
    {
        String selectQuery = "SELECT \"model\".\"value\",\"model\".\"name\"";
        
        String fromQuery = " FROM \"model\"";
        
        String whereQuery = " WHERE \"id_user\"=" + userId + " AND " +
                "\"model\".\"path_unit\"='" + pathUnits.get("model") + "';";
        
        selectQuery += fromQuery + whereQuery;
        
        System.out.println("selectQuery: " + selectQuery);
        
        HashMap<String, String> result = new HashMap();
        
        try (Statement results = jdbcConn.createStatement()) {
            try (ResultSet rs = results.executeQuery(selectQuery)) {
                
                rs.next();
                result.put("name", rs.getString("name"));
       
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] SELECT Query: " + selectQuery);
        }     
        
        return result;
    }     
    
    
    public boolean insertRow(String tableName, Map<String, String> fields)
    {
        
//        dataQuery.executeUpdate("INSERT INTO ORDER_ITEMS (CLIENT_ID, ORDER_ID, ITEM_ID) values (1, 1, "+i+")");
        boolean flag = true; 
        
        ResultSet rs = null;
        
        StringBuilder insertQueryBuilder = new StringBuilder();
        
        insertQueryBuilder.append("INSERT INTO \"" + tableName + "\" ");
        
        String fieldsKeysQuery = "(";
        String fieldsValuesQuery = "(";
        
        
        int i =0;
        
        for(String fieldName:fields.keySet())
        {
            if(i < fields.size() - 1)fieldsKeysQuery += "\"" + fieldName + "\", ";
            else fieldsKeysQuery += "\"" + fieldName + "\") ";
            
            if(isTextField(fieldName,fields.get(fieldName)))
            {
                if(i < fields.size() - 1)fieldsValuesQuery += "'" + fields.get(fieldName) + "', ";
                else fieldsValuesQuery += "'" + fields.get(fieldName) + "') ";     
            }else{
                if(i < fields.size() - 1)fieldsValuesQuery += "" + fields.get(fieldName) + ", ";
                else fieldsValuesQuery += "" + fields.get(fieldName) + ") ";                
            }
                   
            
            i++;
        }
        
        
        insertQueryBuilder.append(fieldsKeysQuery);
        insertQueryBuilder.append(" VALUES ");
        insertQueryBuilder.append(fieldsValuesQuery);
        
        String insertQuery = insertQueryBuilder.toString();

        System.out.println("INSERT Query: " + insertQuery);
        
        
        try (Statement results = jdbcConn.createStatement()) {       
//            results.executeQuery(insertQuery);
            results.execute(insertQuery);
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] INSERT Query: " + insertQuery);
            flag = false;
            return flag;
        }
        
        return flag;
    }
    
    public Long getMaxId(String tableName)
    {
        Long resultValue = null;
        
        if(countAll(tableName) <= 0){
            System.out.println("countAll(tableName): " + countAll(tableName));
            return new Long(0);
        }
        
        String selectQuery = "SELECT max(id)";
        String fromTablesQuery = " FROM \"" + tableName + "\" ";

        selectQuery += fromTablesQuery;

        try (Statement results = jdbcConn.createStatement()) {   
            try(ResultSet rs = results.executeQuery(selectQuery))
            {
            rs.next();
            resultValue =  rs.getLong("max");
            }catch(Exception ex){
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] GET_MAX_ID Query: " + selectQuery);
            return resultValue;
        }
        
        return resultValue;
    }
    
    public void deleteFromTableById(String tableName, Long id)
    {
        String resultValue = null;
        
        String selectQuery = "DELETE ";
        String fromTablesQuery = " FROM \"" + tableName + "\" ";
        String whereConditionsQuery = " WHERE \"id\"=" + id + " ";
        
        selectQuery += fromTablesQuery + whereConditionsQuery;

        try (Statement results = jdbcConn.createStatement()) {   
            try(ResultSet rs = results.executeQuery(selectQuery))
            {
                rs.next();
                resultValue =  rs.getObject(1, String.class);
            }catch(Exception ex){
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] DELETE MODELs: " + selectQuery + "\n\rResult: " + resultValue);
        }  
    }
    
    public Long countByCondition(String tableName, String whereIdKey, String whereIdValue)
    {
        Long resultValue = null;
        
        String selectQuery = "SELECT count(*)";
        String fromTablesQuery = " FROM \"" + tableName + "\" ";
        String whereConditionsQuery = " WHERE \"" + whereIdKey + "\"=" + whereIdValue + " ";

        selectQuery += fromTablesQuery + whereConditionsQuery;

        try (Statement results = jdbcConn.createStatement()) {   
            try(ResultSet rs = results.executeQuery(selectQuery))
            {
                rs.next();
                resultValue =  rs.getLong("count");
            }catch(Exception ex){
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] COUNT MODELs: " + selectQuery);
            return resultValue;
        }
        
        return resultValue;
    }  
    
    public Long countAll(String tableName)
    {
        Long resultValue = null;
        
        String selectQuery = "SELECT count(*)";
        String fromTablesQuery = " FROM \"" + tableName + "\" ";

        selectQuery += fromTablesQuery;

        try (Statement results = jdbcConn.createStatement()) {   
            try(ResultSet rs = results.executeQuery(selectQuery))
            {
                rs.next();
                resultValue =  rs.getLong("count");
            }catch(Exception ex){
                
            }
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] COUNT MODELs: " + selectQuery);
            return resultValue;
        }
        
        if(resultValue == null) return new Long(0);
        else return resultValue;
    }
    
    
    public boolean isTextField(String fieldName, String fieldValue)
    {
        if(fieldName.substring(0, 1).equals("id") || fieldName.equals("type")) return false; //type is integer, all prefixed id are long or integer
        if(fieldValue.equals("true") || fieldValue.equals("false")) return false; //booleans are not text too
        
        return true;
    }    
    
    
    public boolean updateQuery(String tableName, Map<String, String> setFields, Map<String, String> whereFields)
    {
//        dataQuery.executeUpdate("INSERT INTO ORDER_ITEMS (CLIENT_ID, ORDER_ID, ITEM_ID) values (1, 1, "+i+")");
        boolean flag = true; 
        
        ResultSet rs = null;
        
        StringBuilder updateQueryBuilder = new StringBuilder();
        
        updateQueryBuilder.append("UPDATE \"" + tableName + "\" SET ");
        
        String setFieldsQuery = "";
        String whereFieldsQuery = "";
        
        
        int i =0;
        
        for(String fieldName:setFields.keySet())
        {
            if(i < setFields.size() - 1)setFieldsQuery += "\"" +fieldName + "\"='" + setFields.get(fieldName) + "' AND ";
            else setFieldsQuery += "\""+ fieldName + "\"='" + setFields.get(fieldName) + "' ";
            
            
            i++;
        }
        
        i = 0;
        
        whereFieldsQuery += " WHERE ";
        
        for(String fieldName:whereFields.keySet())
        {
            if(i < whereFields.size() - 1)whereFieldsQuery += "\"" + fieldName + "\"='" + whereFields.get(fieldName) + "' AND ";
            else whereFieldsQuery += "\"" + fieldName + "\"='" + whereFields.get(fieldName) + "' ";
            
            
            i++;
        }        
        
        
        
        
        updateQueryBuilder.append(setFieldsQuery);
        updateQueryBuilder.append(whereFieldsQuery);
        
        String updateQuery = updateQueryBuilder.toString();

        System.out.println("UPDATE Query: " + updateQuery);
        
        
        try (Statement results = jdbcConn.createStatement()) {       
//            results.executeQuery(insertQuery);
            results.executeUpdate(updateQuery);
        }
        catch(Exception ex){
            System.out.println("[EXCEPTION] UPDATE Query: " + updateQuery);
            flag = false;
            return flag;
        }
        
        return flag;    
    }
}

