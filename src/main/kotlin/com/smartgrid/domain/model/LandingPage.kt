package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HeroSection(
    val title: String,
    val subtitle: String,
    val primaryButtonText: String,
    val secondaryButtonText: String,
    val stats: List<HeroStat>
)

@Serializable
data class HeroStat(
    val value: String,
    val label: String
)

@Serializable
data class Feature(
    val id: String,
    val icon: String,
    val title: String,
    val description: String
)

@Serializable
data class HowItWorksStep(
    val step: Int,
    val icon: String,
    val title: String,
    val description: String
)

@Serializable
data class PlatformStat(
    val value: String,
    val label: String
)

@Serializable
data class CtaSection(
    val title: String,
    val subtitle: String,
    val primaryButtonText: String,
    val secondaryButtonText: String
)

@Serializable
data class FooterLink(
    val label: String,
    val url: String
)

@Serializable
data class FooterColumn(
    val title: String,
    val links: List<FooterLink>
)

@Serializable
data class FooterSection(
    val companyName: String,
    val companyDescription: String,
    val columns: List<FooterColumn>,
    val copyright: String,
    val socialLinks: List<SocialLink>
)

@Serializable
data class SocialLink(
    val platform: String,
    val url: String
)

@Serializable
data class NavItem(
    val label: String,
    val url: String
)

@Serializable
data class LandingPageData(
    val navigation: List<NavItem>,
    val hero: HeroSection,
    val features: FeaturesSection,
    val howItWorks: HowItWorksSection,
    val stats: StatsSection,
    val cta: CtaSection,
    val footer: FooterSection
)

@Serializable
data class FeaturesSection(
    val badge: String,
    val title: String,
    val subtitle: String,
    val items: List<Feature>
)

@Serializable
data class HowItWorksSection(
    val badge: String,
    val title: String,
    val subtitle: String,
    val steps: List<HowItWorksStep>
)

@Serializable
data class StatsSection(
    val badge: String,
    val title: String,
    val items: List<PlatformStat>
)
