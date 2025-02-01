package com.example.sideproject.domain.user.entity;

public enum TechStack1 {
    // 언어
    JAVA("Java", "java-icon"),
    PYTHON("Python", "python-icon"),
    JAVASCRIPT("JavaScript", "javascript-icon"),
    TYPESCRIPT("TypeScript", "typescript-icon"),
    GO("Go", "go-icon"),
    CSHARP("CSharp", "csharp-icon"),
    CPP("Cpp", "cpp-icon"),

    // 프레임워크
    SPRING("Spring", "spring-icon"),
    REACT("React", "react-icon"),
    VUE("Vue.js", "vue-icon"),
    DJANGO("Django", "django-icon"),
    NEXT("Next", "next-icon"),
    NEST("Nest", "nest-icon"),


    // 데이터베이스
    MYSQL("MySQL", "mysql-icon"),
    POSTGRESQL("PostgreSQL", "postgresql-icon"),
    MONGODB("MongoDB", "mongodb-icon"),

    // DevOps
    DOCKER("Docker", "docker-icon"),
    AWS("AWS", "aws-icon"),
    GIT("Git", "git-icon");

    private final String displayName;
    private final String iconClass;

    TechStack1(String displayName, String iconClass) {
        this.displayName = displayName;
        this.iconClass = iconClass;
    }

    public String getDisplayName() {
        return displayName;
    }
}