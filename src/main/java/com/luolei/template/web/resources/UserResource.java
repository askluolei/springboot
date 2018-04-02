package com.luolei.template.web.resources;

import com.luolei.template.domain.User;
import com.luolei.template.repository.UserRepository;
import com.luolei.template.support.R;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author luolei
 * @createTime 2018-03-28 22:23
 */
@RestController
@RequestMapping(path = {"/user"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Timed(value = "userResource")
public class UserResource {

    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public R addUser(@RequestBody User user) {
        return R.ok(userRepository.save(user));
    }

    @DeleteMapping("{id}")
    public R deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return R.ok();
    }

    @PutMapping
    public R updateUser(@RequestBody User user) {
        return R.ok(userRepository.save(user));
    }

    @GetMapping("{id}")
    public R findById(@PathVariable Long id) {
        return R.ok(userRepository.findById(id));
    }
}
