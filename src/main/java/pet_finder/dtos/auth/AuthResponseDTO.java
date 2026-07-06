package pet_finder.dtos.auth;

public record AuthResponseDTO(String token, long id, String nombre, String apellido, String rol) {}

