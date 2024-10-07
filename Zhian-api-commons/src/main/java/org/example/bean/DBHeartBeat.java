package org.example.bean;

import org.example.enums.DBTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DBHeartBeat {
    @Autowired
    private DataSource masterDataSource;

    @Autowired
    private DataSource slave1DataSource;

    @Autowired
    private DataSource slave2DataSource;

    @Scheduled(fixedDelay = 5000) // 每隔5秒执行一次
    public void checkDatabaseStatus() {
        DBContextHolder.setDBAvailableMap(DBTypeEnum.MASTER,isDatabaseAvailable(masterDataSource));
        DBContextHolder.setDBAvailableMap(DBTypeEnum.SLAVE1,isDatabaseAvailable(slave1DataSource));
        DBContextHolder.setDBAvailableMap(DBTypeEnum.SLAVE2,isDatabaseAvailable(slave2DataSource));
    }

    private boolean isDatabaseAvailable(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            // 执行测试查询，例如 SELECT 1
            // 如果查询成功，则返回 true，说明数据库可用
            if(connection != null && !connection.isClosed()){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT 1");
                if(resultSet.next()){
                    return true;
                }
            }
        } catch (SQLException e) {
            // 发生异常，则返回 false，说明数据库不可用
            return false;
        }
        return false;
    }

}
