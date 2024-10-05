package org.example.service;

import org.example.entity.UserCenter;
import org.example.resp.ResponseMessage;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserCenterService {

    public UserCenter getUserById(Integer id);

    public List<UserCenter> getEmployeeMsg(Integer id);

    public int insertUserMsg(@RequestBody UserCenter user);

    public ResponseMessage updateUserMsg(@RequestBody UserCenter user);

    public int postcapychabyphone(String phone,Integer id);

    public int updatePassword(int id,String password);


}
