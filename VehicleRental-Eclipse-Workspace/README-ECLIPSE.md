# Eclipse Workspace — Vehicle Rental Management System

This bundle contains the project in **Eclipse Dynamic Web Project** format, ready to import
into any Eclipse IDE (2024-09 or newer, with Java 21).

## Contents

| Project (Eclipse name) | Folder | Type | Description |
|---|---|---|---|
| `vehicle-rental-web` | `VehicleRental-Web/` | Maven WAR + **Dynamic Web Project** (WTP facets: Dynamic Web Module 6.0, Java 21) | The full Spring Boot 4 + MySQL + Thymeleaf application |
| `vehicle-rental-legacy-cli` | `VehicleRental-Legacy-CLI/` | Plain Java project | The original console application, kept for reference |

## Import into Eclipse

1. Launch Eclipse and choose (or create) a workspace.
2. **File → Import → Maven → Existing Maven Projects** → browse to the `VehicleRental-Web`
   folder → Finish. Maven downloads dependencies on first import.
   The project appears with a `src/main/webapp` node and Dynamic Web Module facet —
   i.e. a Dynamic Web Project managed by Maven (m2e-wtp).
3. For the legacy CLI: **File → Import → General → Existing Projects into Workspace** →
   select `VehicleRental-Legacy-CLI` → Finish.

## Running the web app

**Option A — embedded server (simplest, no extra setup):**
- Prereq: MySQL running locally; credentials in `src/main/resources/application.properties`
- Right-click project → **Run As → Spring Boot App** (or `./mvnw spring-boot:run`)
- Open http://localhost:8080

**Option B — external Tomcat via "Run on Server" (classic Dynamic Web Project flow):**
1. The Tomcat runtime must support Servlet 6.x (Apache Tomcat 11).
2. **Window → Preferences → Server → Runtime Environments → Add** → Apache Tomcat 11 → point to your Tomcat install.
3. Right-click project → **Run As → Run on Server** → choose the Tomcat 11 runtime.
4. Open http://localhost:8080/vehicle-rental/ (context root: `vehicle-rental`).

**Option C — command line WAR:**
```bash
java -jar target/vehicle-rental-0.0.1-SNAPSHOT.war    # embedded, port 8080
# or drop the WAR into Tomcat's webapps/ folder       # external, context /vehicle-rental
```

## Notes

- The database `vehicle_rental` (schema + seed data) is created automatically on first run.
- `open-in-view=true` in application.properties is intentional — do not set it to false
  (see KT.md §6 for all gotchas).
- The build produces `target/vehicle-rental-0.0.1-SNAPSHOT.war` — a standalone-executable WAR
  that is simultaneously deployable to external Tomcat.
