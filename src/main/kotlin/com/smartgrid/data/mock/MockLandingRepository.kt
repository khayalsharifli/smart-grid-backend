package com.smartgrid.data.mock

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.LandingRepository

class MockLandingRepository : LandingRepository {

    private val navigation = listOf(
        NavItem(label = "About", url = "#about"),
        NavItem(label = "Features", url = "#features"),
        NavItem(label = "How It Works", url = "#how-it-works"),
        NavItem(label = "Stats", url = "#stats")
    )

    private val hero = HeroSection(
        title = "The Future of Energy is Decentralized",
        subtitle = "Join the clean energy revolution. Buy, sell, and trade renewable energy directly with your community — no middleman, full transparency.",
        primaryButtonText = "Get Started",
        secondaryButtonText = "Watch Demo",
        stats = listOf(
            HeroStat(value = "2,400+", label = "Active Prosumers"),
            HeroStat(value = "15 GWh", label = "Energy Traded"),
            HeroStat(value = "98.6%", label = "Uptime")
        )
    )

    private val features = FeaturesSection(
        badge = "Features",
        title = "Why SmartGrid?",
        subtitle = "Built for the next generation of energy infrastructure. Trade smarter, live greener.",
        items = listOf(
            Feature(
                id = "feature-p2p",
                icon = "arrows-exchange",
                title = "Peer-to-Peer Trading",
                description = "Trade energy directly with your neighbors. Our blockchain-powered marketplace eliminates intermediaries and reduces costs."
            ),
            Feature(
                id = "feature-contracts",
                icon = "file-contract",
                title = "Smart Contracts",
                description = "Automated and transparent energy transactions powered by Ethereum smart contracts. Every trade is verifiable on the blockchain."
            ),
            Feature(
                id = "feature-analytics",
                icon = "chart-line",
                title = "Real-time Analytics",
                description = "Monitor your energy consumption and production in real-time with interactive dashboards and AI-powered insights."
            )
        )
    )

    private val howItWorks = HowItWorksSection(
        badge = "How It Works",
        title = "Start Trading in Minutes",
        subtitle = "Three simple steps to join the decentralized energy revolution.",
        steps = listOf(
            HowItWorksStep(
                step = 1,
                icon = "wallet",
                title = "Create Your Wallet",
                description = "Sign up and link your blockchain wallet to get started. Your crypto wallet secures your energy tokens and transactions."
            ),
            HowItWorksStep(
                step = 2,
                icon = "bolt",
                title = "List Your Energy",
                description = "If you produce renewable energy via solar panels, wind turbines, or other sources, list it on the marketplace at your price."
            ),
            HowItWorksStep(
                step = 3,
                icon = "exchange",
                title = "Earn & Trade",
                description = "Buy renewable energy from local producers at the best price. Earn tokens for green energy contributions and grow your portfolio."
            )
        )
    )

    private val stats = StatsSection(
        badge = "Platform Statistics",
        title = "Trusted by Energy Pioneers Worldwide",
        items = listOf(
            PlatformStat(value = "2,400+", label = "Active Prosumers"),
            PlatformStat(value = "15 GWh", label = "Energy Traded"),
            PlatformStat(value = "$2.1M", label = "Token Savings"),
            PlatformStat(value = "98.6%", label = "Return Rate")
        )
    )

    private val cta = CtaSection(
        title = "Ready to Power the Future?",
        subtitle = "Join thousands of energy pioneers trading renewable power on the blockchain. Free to join. Easy to use.",
        primaryButtonText = "Join SmartGrid",
        secondaryButtonText = "Watch Demo"
    )

    private val footer = FooterSection(
        companyName = "SmartGrid",
        companyDescription = "The next generation energy trading platform powered by blockchain technology. Trade renewable energy peer-to-peer.",
        columns = listOf(
            FooterColumn(
                title = "Products",
                links = listOf(
                    FooterLink(label = "Marketplace", url = "/marketplace"),
                    FooterLink(label = "Analytics", url = "/analytics"),
                    FooterLink(label = "Smart Meters", url = "/meters"),
                    FooterLink(label = "Wallet", url = "/wallet")
                )
            ),
            FooterColumn(
                title = "Company",
                links = listOf(
                    FooterLink(label = "About Us", url = "/about"),
                    FooterLink(label = "Careers", url = "/careers"),
                    FooterLink(label = "Blog", url = "/blog"),
                    FooterLink(label = "Contact", url = "/contact")
                )
            ),
            FooterColumn(
                title = "Resources",
                links = listOf(
                    FooterLink(label = "API Docs", url = "/docs"),
                    FooterLink(label = "Whitepaper", url = "/whitepaper"),
                    FooterLink(label = "FAQ", url = "/faq"),
                    FooterLink(label = "Community", url = "/community")
                )
            )
        ),
        copyright = "© 2026 SmartGrid Platform. All rights reserved.",
        socialLinks = listOf(
            SocialLink(platform = "twitter", url = "https://twitter.com/smartgrid"),
            SocialLink(platform = "github", url = "https://github.com/smartgrid"),
            SocialLink(platform = "linkedin", url = "https://linkedin.com/company/smartgrid"),
            SocialLink(platform = "discord", url = "https://discord.gg/smartgrid")
        )
    )

    override fun getLandingPageData(): LandingPageData {
        return LandingPageData(
            navigation = navigation,
            hero = hero,
            features = features,
            howItWorks = howItWorks,
            stats = stats,
            cta = cta,
            footer = footer
        )
    }

    override fun getHero(): HeroSection = hero

    override fun getFeatures(): FeaturesSection = features

    override fun getHowItWorks(): HowItWorksSection = howItWorks

    override fun getStats(): StatsSection = stats

    override fun getCta(): CtaSection = cta

    override fun getFooter(): FooterSection = footer
}
