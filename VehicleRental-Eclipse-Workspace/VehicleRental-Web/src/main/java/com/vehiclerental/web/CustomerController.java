package com.vehiclerental.web;

import com.vehiclerental.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

    private final RentalService service;

    public CustomerController(RentalService service) { this.service = service; }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("customers", service.listCustomers());
        return "customers";
    }

    @PostMapping("/customers")
    public String registerCustomer(@RequestParam String name, @RequestParam String phone,
                                   @RequestParam String licenseNo, RedirectAttributes ra) {
        try {
            var c = service.registerCustomer(name, phone, licenseNo);
            ra.addFlashAttribute("success", "Customer " + c.getCode() + " registered.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }
}
