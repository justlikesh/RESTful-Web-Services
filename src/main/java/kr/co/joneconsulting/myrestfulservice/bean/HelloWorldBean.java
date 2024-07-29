package kr.co.joneconsulting.myrestfulservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // setter, getter 합친것
@AllArgsConstructor
public class HelloWorldBean {
    private final String message;

//    public HelloWorldBean(String message){
//        this.message = message;
//    }
}
