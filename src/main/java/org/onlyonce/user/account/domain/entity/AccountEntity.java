package org.onlyonce.user.account.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.onlyonce.user.account.enums.Role;
import org.onlyonce.user.account.enums.Status;


@Table(name = "account")
@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "login_id", nullable = false, length = 30, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Builder
    public AccountEntity(String loginId, String password, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        // 회원 생성 시 외부에서 역할을 지정안해주면, Default로 USER 권한 부여
        if (this.role == null) {
            this.role = Role.USER;
        }
        this.status = Status.ACTIVE;
    }
 }
