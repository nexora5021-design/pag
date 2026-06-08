package com.co.nexora.pag.service;

import com.co.nexora.pag.dto.ChangePasswordRequest;
import com.co.nexora.pag.dto.LoginRequest;
import com.co.nexora.pag.dto.LoginResponse;
import com.co.nexora.pag.model.Empleado;
import com.co.nexora.pag.model.Socio;
import com.co.nexora.pag.repository.EmpleadoRepository;
import com.co.nexora.pag.repository.SocioRepository;
import com.co.nexora.pag.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final EmpleadoRepository empleadoRepository;
    private final SocioRepository socioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(EmpleadoRepository empleadoRepository, SocioRepository socioRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.empleadoRepository = empleadoRepository;
        this.socioRepository = socioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<Socio> socio = socioRepository.findByUsuarioIgnoreCase(request.getUsuario());
        if (socio.isPresent() && passwordEncoder.matches(request.getPassword(), socio.get().getPassword())) {
            if (Boolean.FALSE.equals(socio.get().getEstado())) {
                return null;
            }
            String token = jwtUtil.generateToken(request.getUsuario(), "socio");
            return new LoginResponse(token, "socio", socio.get().getNombre(), socio.get().getId());
        }

        Optional<Empleado> empleado = empleadoRepository.findByUsuarioIgnoreCase(request.getUsuario());
        if (empleado.isPresent() && passwordEncoder.matches(request.getPassword(), empleado.get().getPassword())) {
            if (Boolean.FALSE.equals(empleado.get().getEstado())) {
                return null;
            }
            String token = jwtUtil.generateToken(request.getUsuario(), "empleado");
            return new LoginResponse(token, "empleado", empleado.get().getNombre(), empleado.get().getId());
        }

        return null;
    }

    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean isTokenValid(String token) {
        return jwtUtil.isValid(token);
    }

    public boolean changePassword(ChangePasswordRequest request) {
        Optional<Socio> socio = socioRepository.findByUsuarioIgnoreCase(request.getUsuario());
        if (socio.isPresent() && passwordEncoder.matches(request.getPasswordActual(), socio.get().getPassword())) {
            socio.get().setPassword(passwordEncoder.encode(request.getPasswordNuevo()));
            socioRepository.save(socio.get());
            return true;
        }

        Optional<Empleado> empleado = empleadoRepository.findByUsuarioIgnoreCase(request.getUsuario());
        if (empleado.isPresent() && passwordEncoder.matches(request.getPasswordActual(), empleado.get().getPassword())) {
            empleado.get().setPassword(passwordEncoder.encode(request.getPasswordNuevo()));
            empleadoRepository.save(empleado.get());
            return true;
        }

        return false;
    }

    public Map<String, Object> getTokenInfo(String token) {
        var claims = jwtUtil.getClaims(token);
        Map<String, Object> info = new java.util.HashMap<>();
        info.put("valid", true);
        info.put("usuario", claims.getSubject());
        info.put("rol", claims.get("rol"));
        return info;
    }
}
