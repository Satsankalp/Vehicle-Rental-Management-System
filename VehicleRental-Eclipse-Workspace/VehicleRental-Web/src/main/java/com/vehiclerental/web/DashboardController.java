package com.vehiclerental.web;

import com.vehiclerental.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final RentalService service;

    public DashboardController(RentalService service) { this.service = service; }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("vehicleCount", service.listVehicles().size());
        model.addAttribute("availableCount", service.availableVehicles().size());
        model.addAttribute("customerCount", service.listCustomers().size());
        model.addAttribute("activeCount", service.activeRentals().size());
        model.addAttribute("overdueCount", service.overdueRentals().size());
        model.addAttribute("revenue", service.totalRevenue());
        model.addAttribute("overdue", service.overdueRentals());
        model.addAttribute("activeRentals", service.activeRentals());
        return "index";
    }
}
