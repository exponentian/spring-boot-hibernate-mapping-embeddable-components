package com.example;

import com.example.model.Address;
import com.example.model.Province;
import com.example.model.User;
import com.example.repository.UserRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User mjordan = new User("Michael", "Jordan");
        mjordan.setHomeAddress( new Address("123 King Ave.", "Vancouver", Province.BC, "1A12B2") );
        mjordan.setBillingAddress( new Address("555 Best Rd.", "Toronto", Province.ON, "1M15H3") );
        
        User kobe = new User("Kobe", "Bryant");
        kobe.setHomeAddress( new Address("2 LA St.", "Burnaby", Province.BC, "5L50P0") );
        //kobe.setBillingAddress( new Address("4432 SF St.", "Richmond", Province.BC, "1L55A0") );
        
        this.userRepository.save(mjordan);
        this.userRepository.save(kobe);
        
        System.out.println("\nData is created successfully...\n");
    }
}
