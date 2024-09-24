package org.example.mapper;

import org.example.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select * from product")
    public List<Product> getAllProduct();
}
