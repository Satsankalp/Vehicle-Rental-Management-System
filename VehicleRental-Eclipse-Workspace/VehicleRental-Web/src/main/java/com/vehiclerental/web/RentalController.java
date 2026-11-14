package com.vehiclerental.web;

import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class RentalController {

    private final RentalService service;

    public RentalController(RentalService service) { this.service = service; }

    @GetMapping("/rentals")
    public String rentals(@RequestParam(required = false) String view, Model model) {
        var vehicles = service.listVehicles();
        var customers = service.listCustomers();
        java.util.List<Rental> list;
        String filter;
        switch (view == null ? "" : view) {
            case "active" -> { list = service.activeRentals(); filter = "Active rentals"; }
            case "history" -> { list = service.returnedRentals(); filter = "Rental history"; }
            case "overdue" -> { list = service.overdueRentals(); filter = "Overdue rentals"; }
            default -> { list = service.listRentals(); filter = "All rentals"; }
        }
        model.addAttribute("rentals", list);
        model.addAttribute("filter", filter);
        model.addAttribute("availableVehicles", service.availableVehicles());
        model.addAttribute("allVehicles", vehicles);
        model.addAttribute("customers", customers);
        return "rentals";
    }

    @PostMapping("/rentals")
    public String rent(@RequestParam Long vehicleId, @RequestParam Long customerId,
                       @RequestParam int days, RedirectAttributes ra) {
        try {
            Rental r = service.rentVehicle(vehicleId, customerId, days);
            ra.addFlashAttribute("success", String.format("Rental %s created. Due %s. Estimated cost: %.2f",
                    r.getCode(), r.getDueDate(), r.baseCost()));
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rentals";
    }

    @PostMapping("/rentals/{id}/return")
    public String returnVehicle(@PathVariable Long id,
                                @RequestParam(required = false) LocalDate returnDate,
                                RedirectAttributes ra) {
        try {
            Rental r = service.returnVehicle(id, returnDate == null ? LocalDate.now() : returnDate);
            ra.addFlashAttribute("success", String.format("Rental %s returned. Base: %.2f | Late fee: %.2f | Total due: %.2f",
                    r.getCode(), r.baseCost(), r.getLateFee(), r.totalCost()));
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rentals";
    }
}
