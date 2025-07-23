package jp.co.example.quote_proposal_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // 不要になったためコメントアウト

import jp.co.example.quote_proposal_1.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // 静的リソースや公開ページは誰でもアクセス可能
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**",
                    "/login",
                    "/register",
                    "/home", // homeもpermitAllにしておくのが一般的
                    "/quote/form",      // GET /quote/form (見積もり入力フォーム)
                    "/quote/calculate", // POST /quote/calculate (見積もり計算結果)
                    "/quote/register",  // POST /quote/register (見積もり登録処理)
                    "/quote/detail/**", // GET /quote/detail/{id} (見積もり詳細画面)
                    "/quote/completion",// GET /quote/completion (見積もり完了画面)
                    "/estimates/new",   // 見積もり関連の既存のpermitAllパス
                    "/estimates",
                    "/estimates/result"
                ).permitAll()
                // ユーザー管理関連パスはADMINロールを持つユーザーのみアクセス可能
                .requestMatchers("/users", "/users/**").hasRole("ADMIN")
                // 顧客一覧は認証済みユーザーなら誰でもアクセス可能 (ADMINでなくても可)
                .requestMatchers("/customers", "/customers/**").authenticated()
                // 上記以外のすべてのリクエストは認証が必要
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // ログインページのURL
                .defaultSuccessUrl("/home", true) // ログイン成功後のデフォルトリダイレクト先
                .permitAll() // ログインフォームは誰でもアクセス可能
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // ログアウト処理のURL
                .logoutSuccessUrl("/login?logout") // ログアウト成功後のリダイレクト先
                .permitAll() // ログアウト処理は誰でもアクセス可能
            )
            .csrf(csrf -> csrf.disable()); // 開発中はCSRF保護を無効にすることが多いが、本番環境では有効にすべき

        return http.build();
    }
}
