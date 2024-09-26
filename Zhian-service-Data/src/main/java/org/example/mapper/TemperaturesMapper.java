package org.example.mapper;

import org.example.entity.Temperature;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TemperaturesMapper {

    @Select("select * from temperatures where workshop=#{workshop} order by time desc limit 0,#{number}")
    public List<Temperature> getTemperatureByNumber(String workshop, int number);

    //按时间降序排列
    @Select("select * from temperatures where workshop=#{workshop} and time between #{startTime} and #{endTime} order by time desc")
    public List<Temperature> getTemperatureByTime(String workshop, long startTime, long endTime);

    @Insert("insert into temperatures values (#{workshop},#{time},#{temperature})")
    public int insertTemperature(String workshop,float temperature,long time);

    //查询结果的位置 查询结果的数量
    @Select("select * from temperatures where workshop=#{workshop} order by time desc limit #{startNumber},#{endNumber}")
    public List<Temperature> getTemperatureByPage(String workshop, int startNumber, int endNumber);

    @Select("select time from temperatures where workshop=#{workshop} order by time desc limit 0,1")
    public long getTemperatureLastTime(String workshop);
}
