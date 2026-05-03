rootProject.name = "mametosho"

include("common")
include("cs-api")
include("admin-api")

// "com.mametosho:common" をローカルのサブプロジェクトに解決する
// (各サービスのbuild.gradle.ktsで独立プロジェクト用に "com.mametosho:common" を使うため)
gradle.allprojects {
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.mametosho:common")).using(project(":common"))
        }
    }
}
