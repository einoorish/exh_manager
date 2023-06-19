package com.einoorish.exhibitionsbackend.controller.auth;

import com.einoorish.exhibitionsbackend.config.jwt.TokenProvider;
import com.einoorish.exhibitionsbackend.controller.auth.dto.AuthToken;
import com.einoorish.exhibitionsbackend.controller.auth.dto.login.AuthRequest;
import com.einoorish.exhibitionsbackend.model.user.User;
import com.einoorish.exhibitionsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value="/admin")
	public ResponseEntity<?> adminPing(){

		return ResponseEntity.ok()
				.body("Admin Can Read This");
	}

	public static String extractTokenFromAuthentication(Authentication authentication) {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
			Object credentials = usernamePasswordAuthenticationToken.getCredentials();
			if (credentials instanceof String) {
				return (String) credentials;
			}
		}
		return null;
	}


	@GetMapping(value = "/clear_session")
	public String logout(HttpServletResponse response) throws AuthenticationException {
		SecurityContextHolder.getContext().setAuthentication(null);

		Cookie authCookie = new Cookie("authToken", null);
		authCookie.setHttpOnly(true);
		authCookie.setMaxAge(0);
		authCookie.setPath("/");

		System.out.println("Logged out ");

		response.addCookie(authCookie);

		return "redirect:/";
	}


	@PostMapping(value = "/login")
	public String generateToken(@ModelAttribute AuthRequest authRequest, HttpServletResponse response) throws AuthenticationException {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authRequest.getEmail(),
						authRequest.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(authentication);
		System.out.println("Token is "+token);

		Cookie authCookie = new Cookie("authToken", token);
		authCookie.setHttpOnly(true);
		authCookie.setMaxAge(24*60*60);
		authCookie.setPath("/");

		response.addCookie(authCookie);

//		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
//				.body(new AuthToken(token, authentication.getAuthorities().toString()));
		return "redirect:" + determineTargetUrl(authentication);
	}


	protected String determineTargetUrl(final Authentication authentication) {
		Map<String, String> roleTargetUrlMap = new HashMap<>();
		roleTargetUrlMap.put("PUBLISHER", "/exhibits");
		roleTargetUrlMap.put("ADMIN", "/organizations");

		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (final GrantedAuthority grantedAuthority : authorities) {
			String authorityName = grantedAuthority.getAuthority();
			if(roleTargetUrlMap.containsKey(authorityName)) {
				return roleTargetUrlMap.get(authorityName);
			}
		}

		throw new IllegalStateException();
	}


}