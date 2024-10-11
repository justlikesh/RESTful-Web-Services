package kr.co.joneconsulting.myrestfulservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import kr.co.joneconsulting.myrestfulservice.bean.User;
import kr.co.joneconsulting.myrestfulservice.dao.UserDaoService;
import kr.co.joneconsulting.myrestfulservice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러입니다.") //클래스에 대한 설명
public class UserController {

    private final UserDaoService service;

//    public UserController(UserDaoService service) {   //의존성 주입을 위해서 생성자를 만든다
//        this.service = service;
//    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }


    @Operation(summary = "사용자 정보 조회 API", description = "사용자 ID를 이용해서 사용자 상세 정보 조회를 합니다.")
    //api 오류코드 정리
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "USER NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
        }
    )
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(
            @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable Integer id){
        User user = service.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        EntityModel entityModel = EntityModel.of(user);

        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkTo.withRel("all-users")); // all-users -> http://localhost:8088/users
        return entityModel;
    }
//    "_links": {
//        "all-users": {
//            "href": "http://localhost:8088/users"
//        }
//    }


    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable int id){
        User deleteUser = service.deleteById(id);

        if (deleteUser == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return ResponseEntity.noContent().build();
    }
}
