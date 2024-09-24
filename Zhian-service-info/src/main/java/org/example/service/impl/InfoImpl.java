package org.example.service.impl;

import org.example.entity.Attendance;
import org.example.entity.Employee;
import org.example.entity.Employee_attendance;
import org.example.mapper.AttendanceMapper;
import org.example.mapper.EmployeeMapper;
import org.example.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InfoImpl implements InfoService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Value("${upload.path}")
    private String imagePath;

    @Override
    public String addEmployee(Employee employee) {
        int num = employeeMapper.findCard(0,employee.getCardId());
        if(num<1){
            employeeMapper.addEmployee(employee.getName(),employee.getCardId(),employee.getManagerId(),
                    employee.getSeniority(),employee.getSex(),employee.getPosition(),employee.getHeadshot(),employee.getPhone());
            int id = employeeMapper.getId(employee.getCardId());
            return Integer.toString(id);
        }
        else {
            return "failed";
        }
    }

    @Override
    public String updateEmployee(Employee employee) {
        int num = employeeMapper.findCard(employee.getId(),employee.getCardId());
        if(num<1){
            employeeMapper.updateEmployee(employee.getId(),employee.getName(),employee.getCardId(), employee.getManagerId(),
                    employee.getSeniority(),employee.getSex(),employee.getPosition(),employee.getPhone(),employee.getHeadshot());
            return "succeed";
        }
        else {
            return "failed";
        }
    }

    @Override
    public String deleteEmployee(int id) {
        int num = employeeMapper.deleteEmployee(id);
        if(num<=0){
            return "failed";
        }
        else {
            return "succeed";
        }
    }

    @Override
    public List<Employee> getEmployeeByManager(int managerId, int startNumber, int endNumber) {
        if(managerId<1||endNumber<1) return null;
        List<Employee> employeeList = employeeMapper.getEmployeeByManagerId(managerId,startNumber,endNumber);
        return employeeList;
    }

    @Override
    public Employee getEmployeeById(int id) {
        if(id==0) return null;
        Employee employee = employeeMapper.getEmployeeById(id);
        return employee;
    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        if(name==null) return null;
        List<Employee> employeeList = employeeMapper.getEmployeeByName(name);
        return employeeList;
    }

    @Override
    public List<Employee> getEmployeeByCardId(String cardId) {
        if(cardId==null) return null;
        List<Employee> employeeList = employeeMapper.getEmployeeByCardId(cardId);
        return employeeList;
    }

    @Override
    public List<Employee> getEmployeeByPage(int startNumber, int endNumber) {
        if(endNumber<1) return null;
        List<Employee> employeeList = employeeMapper.getEmployeeByPage(startNumber,endNumber);
        return employeeList;
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeMapper.getAllEmployee();
    }

    @Override
    public List<Employee> getEmployeeByInfo(String info) {
        int id;
        try {
            id = Integer.parseInt(info);
        } catch (Exception e){
            id = 0;
        }
        List<Employee> employeeList = employeeMapper.getEmployeeByInfo(id,info);
        return employeeList;
    }

    @Override
    public List<Employee_attendance> getAttendanceByTime(long time, int startId, int endId) {
        if(endId<1) return null;
        Timestamp date = new Timestamp(time);
        List<Employee_attendance> employeeList = employeeMapper.getEmployeeAttendanceByTime(date);
        List<Employee_attendance> resultList = new ArrayList<Employee_attendance>();
        int i,j;
        for(i=0;i<employeeList.size();i++){
            Employee_attendance employee_attendance = employeeList.get(i);
            if(employee_attendance.getIsAttendance()==null){
                employee_attendance.setIsAttendance(0);
                employeeList.set(i,employee_attendance);
            }
            if(employee_attendance.getNote()==null){
                employee_attendance.setNote("无");
            }
        }
        int flag = 0;
        for(i=0,j=0;i<employeeList.size() && j<startId;i++){
            if(employeeList.get(i).getIsAttendance()==0){
                flag = 0;
                j++;
            }else {
                flag++;
            }
            if(flag==2){
                flag = 0;
                j++;
            }
        }
        j = 0;
        flag = 0;
        for(;i<employeeList.size() && j<endId;i++){
            resultList.add(employeeList.get(i));
            if(employeeList.get(i).getIsAttendance()==0){
                j++;
                flag = 0;
            }else {
                flag++;
            }
            if(flag==2){
                flag = 0;
                j++;
            }
        }
        return resultList;
    }

    @Override
    public List<Employee_attendance> getAttendanceByTimeAndInfo(long time, String info) {
        int id;
        try {
            id = Integer.parseInt(info);
        } catch (Exception e) {
            id = 0;
        }
        Timestamp date = new Timestamp(time);
        List<Employee_attendance> employeeList = employeeMapper.getEmployeeAttendanceByTimeAndInfo(date,id,info);
        for(int i=0;i<employeeList.size();i++){
            Employee_attendance employee_attendance = employeeList.get(i);
            if(employee_attendance.getIsAttendance()==null){
                employee_attendance.setIsAttendance(0);
                employeeList.set(i,employee_attendance);
            }
            if(employee_attendance.getNote()==null){
                employee_attendance.setNote("无");
            }
        }
        return employeeList;
    }

    @Override
    public Map<String, Integer> getAttendanceNum(long time) {
        List<Employee_attendance> employeeList = employeeMapper.getEmployeeAttendanceByTime(new Timestamp(time));
        int haveAttendance = 0;
        int noAttendance = 0;
        int i;
        for(i=0;i<employeeList.size();i++){
            Employee_attendance employee_attendance = employeeList.get(i);
            if(employee_attendance.getIsAttendance()==null || employee_attendance.getIsAttendance()==0){
                noAttendance++;
            }
            else if(employee_attendance.getIsAttendance()==1){
                haveAttendance++;
            }
        }
        Map<String,Integer> numMap = new HashMap<String,Integer>();
        numMap.put("haveAttendance",haveAttendance);
        numMap.put("noAttendance",noAttendance);
        return numMap;
    }

    @Override
    public List<Employee_attendance> getAttendanceById(int id, long startTime, long endTime) {
        if(id<1||endTime==0) return null;
        Timestamp startDate = new Timestamp(startTime);
        Timestamp endDate = new Timestamp(endTime);
        List<Employee_attendance> employeeList = attendanceMapper.getAttendanceById(id,startDate,endDate);
        for(int i=0;i<employeeList.size();i++){
            Employee_attendance employee_attendance = employeeList.get(i);
            if(employee_attendance.getIsAttendance()==null){
                employee_attendance.setIsAttendance(0);
                employeeList.set(i,employee_attendance);
            }
            if(employee_attendance.getNote()==null){
                employee_attendance.setNote("无");
            }
        }
        return employeeList;
    }

    @Override
    public Integer getAttendanceByIdAndTime(int id, long time) {
        if(id<1||time==0) return null;
        Timestamp date = new Timestamp(time);
        Attendance attendance = attendanceMapper.getAttendanceByIdAndTime(id,date);
        if(attendance==null && attendance.getIsAttendance()==0){
            return 0;
        }
        else{
            return 1;
        }
    }

    @Override
    public String uploadImage(MultipartFile file, String filename) {
        try {
            String filePath = imagePath + "employee_" +filename+".jpg";
            File existingFile = new File(filePath);
            if (existingFile.exists()) {
                existingFile.delete();
            }
            file.transferTo(new File(filePath));
            InputStream inputStream = new FileInputStream(new File(filePath));
            BufferedImage originalImage = ImageIO.read(inputStream);
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int squareSize = Math.min(width, height);
            int x = (width - squareSize) / 2;
            int y = (height - squareSize) / 2;
            BufferedImage croppedImage = originalImage.getSubimage(x, y, squareSize, squareSize);
            File outputFile = new File(filePath);
            ImageIO.write(croppedImage,"jpg",outputFile);

            employeeMapper.updateHeadshot(Integer.parseInt(filename),"employee_"+filename+".jpg");
            inputStream.close();
            return "succeed";
        } catch (Exception e) {
            System.out.println(e);
            return "failed";
        }
    }

    @Override
    public String uploadNormalImage(MultipartFile file, String filename) {
        try {
            String filePath = imagePath + filename;
            File existingFile = new File(filePath);
            if (existingFile.exists()) {
                existingFile.delete();
            }
            file.transferTo(new File(filePath));
            InputStream inputStream = new FileInputStream(new File(filePath));
            BufferedImage originalImage = ImageIO.read(inputStream);
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int squareSize = Math.min(width, height);
            int x = (width - squareSize) / 2;
            int y = (height - squareSize) / 2;
            BufferedImage croppedImage = originalImage.getSubimage(x, y, squareSize, squareSize);

            File outputFile = new File(filePath);
            int dotIndex = filename.lastIndexOf(".");
            String format = filename.substring(dotIndex + 1);
            ImageIO.write(croppedImage,format,outputFile);
            inputStream.close();
            return "succeed";
        } catch (Exception e) {
            return "failed";
        }
    }
}
