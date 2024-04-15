package org.example;

import org.example.Mapper.UserMpper;
import org.example.MineBatisUtils.MapperJdkProxyFactory;

public class Main {
    public static void main(String[] args) {
        UserMpper userMpper = MapperJdkProxyFactory.getMapper(UserMpper.class);
        System.out.println(userMpper.getOneUser(1, "wzy"));
        System.out.println(userMpper.getAllUser());
    }
}