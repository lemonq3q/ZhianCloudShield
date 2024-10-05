package org.example.mapper;

import org.example.entity.Humidity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HumiditysMapper {
    @Select("select * from humiditys where workshop=#{workshop} order by time desc limit 0,#{number}")
    public List<Humidity> getTemperatureByNumber(String workshop, int number);

    @Select("select * from humiditys where workshop=#{workshop} and time between #{startTime} and #{endTime} order by time desc")
    public List<Humidity> getTemperatureByTime(String workshop, long startTime, long endTime);

    @Insert("insert into humiditys values (#{workshop},#{time},#{humidity})")
    public int insertHumidity(String workshop,float humidity,long time);

    @Select("select * from humiditys where workshop=#{workshop} order by time desc limit #{startNumber},#{endNumber}")
    public List<Humidity> getHumidityByPage(String workshop, int startNumber, int endNumber);

    @Select("select time from humiditys where workshop=#{workshop} order by time desc limit 0,1")
    public long getHumiLastTime(String workshop);

    public List<Humidity> getHumidityByIdList(List<Integer> idList);
}
