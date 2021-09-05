package ru.sklyarov.spring.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sklyarov.spring.security.entities.Authority;
import ru.sklyarov.spring.security.entities.Role;
import ru.sklyarov.spring.security.entities.User;
import ru.sklyarov.spring.security.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(getAuthorities(user.getRoles())));
    }

    // Раюочий метод, но какой-то ужасный. Лучше делать с помощью getGrantedAuthorities?
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> sga;
        sga = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        Collection<SimpleGrantedAuthority> sgr;
        sgr = roles.stream()
                .map(role -> role.getAuthoritiesList().stream().map(SimpleGrantedAuthority::new).findFirst().orElseThrow()).collect(Collectors.toList());

        sga.addAll(sgr);
        return sga;
    }

    private List<String> getAuthorities(Collection<Role> roles) {
        List<String> authorities = new ArrayList<>();
        List<Authority> authCollections = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(role.getName());
            authCollections.addAll(role.getAuthorities());
        }

        for (Authority authority: authCollections) {
            authorities.add(authority.getName());
        }

        return authorities;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(List<String> authorities){
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
