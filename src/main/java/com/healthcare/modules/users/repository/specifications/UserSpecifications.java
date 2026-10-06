package com.healthcare.modules.users.repository.specifications;
import com.healthcare.modules.users.entity.UserEntity;
import com.healthcare.modules.users.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<UserEntity> search(String search) {
        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("username")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")),
                            pattern
                    )
            );
        };
    }

    public static Specification<UserEntity> hasRole(UserRole userRole) {
        return (root, query, criteriaBuilder) ->
                userRole == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("role"), userRole);
    }

    public static Specification<UserEntity> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("active"), isActive);
    }

}
