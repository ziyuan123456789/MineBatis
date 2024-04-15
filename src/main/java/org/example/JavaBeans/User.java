package org.example.JavaBeans;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ziyuan
 * @since 2024.04
 */
@Data
public class User {
    private int userID;
    private String username;
    private String role;
    private String password;
    private String salt;
    private String telephone;
    private LocalDateTime regTime;
    private String enabled;
}
