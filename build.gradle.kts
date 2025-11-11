plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.detekt)
}

val detektFormatting = libs.detekt.formatting

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        config.from(rootProject.files("lint/detekt-config.yml"))
    }

    dependencies {
        detektPlugins(detektFormatting)
    }
}