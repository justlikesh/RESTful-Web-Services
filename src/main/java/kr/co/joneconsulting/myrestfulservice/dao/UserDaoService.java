package kr.co.joneconsulting.myrestfulservice.dao;

import kr.co.joneconsulting.myrestfulservice.bean.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;    // 사용자수

    static {
        users.add(new User(1,"Kenneth", new Date(),"test1","1111"));
        users.add(new User(2,"Alice", new Date(),"test2","1111"));
        users.add(new User(3,"Aaron", new Date(),"test3","1111"));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if(user.getId() == null){
            user.setId(++usersCount);
        }

        if(user.getJoinDate()==null){
            user.setJoinDate(new Date());
        }

        users.add(user);

        return user;
    }

    public User findOne(int id){
        for (User user: users){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()){
            User user = iterator.next();

            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }
}
