//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .cors(Customizer.withDefaults()) // ✅ ENABLE CORS IN SECURITY
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers(HttpMethod.OPTIONS).permitAll() // ✅ Allow preflight
//                        .pathMatchers("/auth/**").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(Customizer.withDefaults())
//                )
//                .build();
//    }
//}