package com.example.rememberme.service;

import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {

    private final PersonRepository personRepository;
//    private final AuthenticationManager authenticationManager;

//    public void checkAuth(HttpServletRequest request) {
//        List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
//        String token = "";
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("token")) token = cookie.getValue();
//        }
//        Person person = personRepository.findByToken(token);
//        if (person == null) throw new AuthException("Auth token is invalid");
//        authenticated(person);
//    }

//    private void authenticated(Person person) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(person.getLogin(), person.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
}
