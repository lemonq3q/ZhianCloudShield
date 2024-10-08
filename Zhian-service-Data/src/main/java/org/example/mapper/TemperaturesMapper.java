package org.example.mapper;

import org.apache.ibatis.annotations.Update;
import org.example.entity.Temperature;
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

    public int insertTemperature(Temperature temperature);

    //查询结果的位置 查询结果的数量
    @Select("select * from temperatures where workshop=#{workshop} order by time desc limit #{startNumber},#{endNumber}")
    public List<Temperature> getTemperatureByPage(String workshop, int startNumber, int endNumber);

    @Select("select time from temperatures where workshop=#{workshop} order by time desc limit 0,1")
    public long getTemperatureLastTime(String workshop);

    @Select("select * from temperatures where id = 1")
    public Temperature getTest();

    @Update("update temperatures set temperature = #{temperature} where id = 1")
    public int updateTest(float temperature);

    public List<Temperature> getTemperatureByIdList(List<Integer> idList);

    @Select("select id from temperatures where workshop = #{workshop} order by id desc")
    public List<Integer> getIdList(String workshop);

    @Select("select distinct workshop from temperatures")
    public List<String> getWorkshopList();

    @Select("select * from temperatures where workshop = #{workshop} order by id desc limit 1")
    public Temperature getLastTemperatureByWorkshop(String workshop);
}
