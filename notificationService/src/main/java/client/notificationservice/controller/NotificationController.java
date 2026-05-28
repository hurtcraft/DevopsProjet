package client.notificationservice.controller;

import client.notificationservice.model.NotificationRequest;
import client.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notify")
@Tag(name = "Notification API", description = "Gestion des notifications liées aux événements Todo")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @Operation(
            summary = "Envoyer une notification",
            description = "Envoie une notification basée sur un événement (création, mise à jour, suppression de todo)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification envoyée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<String> notify(@RequestBody NotificationRequest request) {

        String response = notificationService.sendNotification(request);

        return ResponseEntity.ok(response);
    }
}