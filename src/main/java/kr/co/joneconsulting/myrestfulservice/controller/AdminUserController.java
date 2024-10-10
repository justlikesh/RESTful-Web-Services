package kr.co.joneconsulting.myrestfulservice.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.validation.Valid;
import kr.co.joneconsulting.myrestfulservice.bean.AdminUser;
import kr.co.joneconsulting.myrestfulservice.bean.User;
import kr.co.joneconsulting.myrestfulservice.dao.UserDaoService;
import kr.co.joneconsulting.myrestfulservice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private final UserDaoService service;

    // /admin/users/{id}
    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser4Admin(@PathVariable Integer id){
        User user = service.findOne(id);

        AdminUser adminUser = new AdminUser();

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        } else {
//            adminUser.setName(user.getName());
            BeanUtils.copyProperties(user, adminUser);  // 앞에있는게 소스, 뒤에있는게 타겟 소스-> 타겟
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate","ssn"); // 공개하려는 내용 적기 (제약걸기)

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);  // 어디다가 적용할건지

        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);  // FilterProvider 로 결과 데이터를 만들어서 반환
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users = service.findAll();

        List<AdminUser> adminUsers = new ArrayList<>();
        AdminUser adminUser = null;
        for(User user : users){             // 전체 유저 데이터 값을
            adminUser = new AdminUser();        //adminuser 데이터 값으로 바꿔서
            BeanUtils.copyProperties(user, adminUser); //프로포티를 복사해서 리스트에 추가하는 작업
            adminUsers.add(adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUsers);
        mapping.setFilters(filters);

        return mapping;
    }

}
