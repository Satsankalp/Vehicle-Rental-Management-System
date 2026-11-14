package com.vehiclerental.web;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.RentalService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VehicleController {

    private final RentalService service;

    public VehicleController(RentalService service) { this.service = service; }

    @GetMapping("/vehicles")
    public String vehicles(@RequestParam(required = false) String q,
                           @RequestParam(required = false) String view,
                           Model model) {
        java.util.List<Vehicle> list;
        if (q != null && !q.isBlank()) {
            list = service.searchVehicles(q);
            model.addAttribute("filter", "Search: \"" + q + "\"");
        } else if ("available".equals(view)) {
            list = service.availableVehicles();
            model.addAttribute("filter", "Available only");
        } else {
            list = service.listVehicles();
            model.addAttribute("filter", "All vehicles");
        }
        model.addAttribute("vehicles", list);
        return "vehicles";
    }

    public record VehicleForm(@NotBlank String make, @NotBlank String model,
                              @NotNull Vehicle.Type type, double dailyRate) {}

    @PostMapping("/vehicles")
    public String addVehicle(@RequestParam String make, @RequestParam String model,
                             @RequestParam Vehicle.Type type, @RequestParam double dailyRate,
                             RedirectAttributes ra) {
        try {
            Vehicle v = service.addVehicle(make, model, type, dailyRate);
            ra.addFlashAttribute("success", "Vehicle " + v.getCode() + " added.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vehicles";
    }

    @PostMapping("/vehicles/{id}/delete")
    public String removeVehicle(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.removeVehicle(id);
            ra.addFlashAttribute("success", "Vehicle removed.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vehicles";
    }
}
