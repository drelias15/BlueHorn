package com.example.bluehorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message",
                    "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/update/{id}")
    public String userUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("message", messageRepository.findById(id).get());
        return "messageform";
    }
    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userService.getCurrentUser());
        messageRepository.deleteById(id);
        return "redirect:/secure";
    }

    @RequestMapping("/secure")
    public String secure(Model model) {
        String username = userService.getCurrentUser().getUsername();
      model.addAttribute("messages", messageRepository.findBySentBy(username));
        model.addAttribute("user", userService.getCurrentUser());
        return "secure";
    }
    @GetMapping("/add")
    public String addTask(Model model){
        model.addAttribute("message", new Message());
        model.addAttribute("user", userService.getCurrentUser());
        return "messageform";
    }
    @PostMapping("/process")
    public String processForm( @ModelAttribute Message message, BindingResult result, Model model)
    {
        String username = userService.getCurrentUser().getUsername();
        message.setSentBy(username);
        messageRepository.save(message);
        model.addAttribute("messages", messageRepository.findBySentBy(username));

        model.addAttribute("user", userService.getCurrentUser());
        return "secure";
    }

    @RequestMapping("/feeds")
    public String feeds(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
        return "feeds";
    }
    @RequestMapping("/profile/{username}")
    public String showCourse(@PathVariable("username") String username, Model model){
        username = userService.getCurrentUser().getUsername();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "profile";
    }

}